package com.alibaihaqi.simplesteph.grpc.blog.server;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.proto.blog.*;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.bson.Document;
import org.bson.types.ObjectId;

import static com.mongodb.client.model.Filters.eq;


public class BlogServiceImpl extends BlogServiceGrpc.BlogServiceImplBase {

    private MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    private MongoDatabase database = mongoClient.getDatabase("mydb");
    private MongoCollection<Document> collection = database.getCollection("blog");

    @Override
    public void createBlog(CreateBlogRequest request, StreamObserver<CreateBlogResponse> responseObserver) {

        System.out.println("Received Blog Request");
        Blog blog = request.getBlog();

        Document doc = new Document("author_id", blog.getAuthorId())
            .append("title", blog.getTitle())
            .append("content", blog.getContent());

        System.out.println("Inserting blog...");
        // we insert (create) the document in MongoDB
        collection.insertOne(doc);

        // we retrieve the MongoDB generatedID
        String id = doc.getObjectId("_id").toString();
        System.out.println("Inserted blog..." + id);

        CreateBlogResponse response = CreateBlogResponse.newBuilder()
                .setBlog(blog.toBuilder().setId(id))
                .build();

        responseObserver.onNext(response);

        responseObserver.onCompleted();
    }

    @Override
    public void readBlog(ReadBlogRequest request, StreamObserver<ReadBlogResponse> responseObserver) {

        System.out.println("Received Read Blog Request");
        String blogId = request.getBlogId();

        Document result = null;
        try {
            System.out.println("Searching for a blog");
            result = collection.find(eq("_id", new ObjectId(blogId)))
                    .first();
        } catch (Exception e) {
            responseObserver.onError(
                    Status.NOT_FOUND
                            .withDescription("The blog with the corresponding id was not found")
                            .augmentDescription((e.getLocalizedMessage()))
                            .asRuntimeException()
            );
        }


        if (result == null) {
            System.out.println("Blog not found");
            responseObserver.onError(
                    Status.NOT_FOUND
                        .withDescription("The blog with the corresponding id was not found")
                        .asRuntimeException()
            );
        } else {
            System.out.println("Blog found and sent back!");
            Blog blog = Blog.newBuilder()
                    .setAuthorId(result.getString("author_id"))
                    .setTitle(result.getString("title"))
                    .setContent(result.getString("content"))
                    .setId(blogId)
                    .build();
            responseObserver.onNext(
                ReadBlogResponse.newBuilder().setBlog(blog).build()
            );

            responseObserver.onCompleted();
        }
    }
}
