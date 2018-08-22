package com.stiva.springbootelasticdemo.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.stiva.springbootelasticdemo.model.Book;
import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.util.*;

@Repository
public class BookDao {
    private final String INDEX ="bookdata";
    private final String TYPE="books";
    private final Gson gson;
    private RestHighLevelClient restHighLevelClient;
    private ObjectMapper objectMapper;

    public BookDao(RestHighLevelClient restHighLevelClient, ObjectMapper objectMapper,Gson gson) {
        this.restHighLevelClient = restHighLevelClient;
        this.objectMapper = objectMapper;
        this.gson=gson;
    }

    public Book insertBook(Book book){
        book.setId(UUID.randomUUID().toString());
        Map dataMap=objectMapper.convertValue(book,Map.class);
        IndexRequest indexRequest=new IndexRequest(INDEX,TYPE,book.getId()).source(dataMap);
        try{
            IndexResponse response=restHighLevelClient.index(indexRequest);
        }catch (ElasticsearchException e){
            e.getDetailedMessage();
        }
        catch (IOException e) {
            e.getLocalizedMessage();
        }
        return book;
    }

    public Map<String,Object> getBookById(String id){
        GetRequest getRequest=new GetRequest(INDEX, TYPE, id);
        GetResponse getResponse=null;
        try{
            getResponse=restHighLevelClient.get(getRequest);

        } catch (IOException e) {
            e.getLocalizedMessage();
        }
        Map<String,Object> sourceAsMap=getResponse.getSourceAsMap();
        return sourceAsMap;
    }

    public Map<String,Object> updateBookById(String id,Book book){
        UpdateRequest updateRequest=new UpdateRequest(INDEX,TYPE,id).fetchSource(true);
        Map<String,Object> error=new HashMap<>();
        error.put("error","Unable to update book");
        try{
            String bookJson=objectMapper.writeValueAsString(book);
            updateRequest.doc(bookJson, XContentType.JSON);
            UpdateResponse updateResponse=restHighLevelClient.update(updateRequest);
            Map<String,Object> sourceAsMap=updateResponse.getGetResult().sourceAsMap();
            return sourceAsMap;
        } catch (JsonProcessingException e) {
            e.getMessage();
        } catch (IOException e) {
            e.getLocalizedMessage();
        }
        return error;
    }

    public void deleteBookById(String id){
        DeleteRequest deleteRequest=new DeleteRequest(INDEX,TYPE,id);
        try{
            DeleteResponse deleteResponse=restHighLevelClient.delete(deleteRequest);
        } catch (IOException e) {
            e.getLocalizedMessage();
        }
    }

    public List<Book> getAll(String text) throws IOException{

        List<Book> bookList=new ArrayList<>();
        QueryBuilder query= QueryBuilders.boolQuery()
                .should(QueryBuilders.queryStringQuery(text)
                        .lenient(true)
                        .field("author")
                        .field("title"));


        SearchRequest searchRequest=new SearchRequest(INDEX);
        searchRequest.types(TYPE);
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        searchSourceBuilder.query(query);
        searchRequest.source(searchSourceBuilder);


        final SearchResponse response=restHighLevelClient.search(searchRequest);

        SearchHits hits=response.getHits();
        SearchHit[] searchHits=hits.getHits();
        for(SearchHit hit:searchHits){
            Book book=gson.fromJson(hit.getSourceAsString(),Book.class);
            book.setId(hit.getId());
            bookList.add(book);
        }
        return bookList;
    }


}
