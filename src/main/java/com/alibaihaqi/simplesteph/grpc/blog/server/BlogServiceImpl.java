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

    private static final MongoClient mongoClient = MongoClients.create("mongodb://localhost:27017");
    private static final MongoDatabase database = mongoClient.getDatabase("mydb");
    private static final MongoCollection<Document> collection = database.getCollection("blog");

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
            Blog blog = documentToBlog(result);
            responseObserver.onNext(
                ReadBlogResponse.newBuilder().setBlog(blog).build()
            );

            responseObserver.onCompleted();
        }
    }

    @Override
    public void updateBlog(UpdateBlogRequest request, StreamObserver<UpdateBlogResponse> responseObserver) {

        System.out.println("Received Blog Request");
        Blog blog = request.getBlog();
        String blogId = blog.getId();

        System.out.println("Searching for a blog so we can update it");
        Document result = null;

        try {
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
            Document replacement = new Document("author_id", blog.getAuthorId())
                    .append("title", blog.getTitle())
                    .append("content", blog.getContent());

            System.out.println("Replacing blog in database...");
            collection.replaceOne(eq("_id", result.getObjectId("_id")), replacement);

            System.out.println("Replaced! Sending as a response");
            responseObserver.onNext(
                    UpdateBlogResponse.newBuilder()
                            .setBlog(documentToBlog(replacement))
                            .build()
            );

            responseObserver.onCompleted();
        }
    }

    @Override
    public void deleteBlog(DeleteBlogRequest request, StreamObserver<DeleteBlogResponse> responseObserver) {

        System.out.println("Received Blog Request");

        String blogId = request.getBlogId();

        System.out.println("Searching for a blog so we can delete it");
        Document result = null;

        try {
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
            System.out.println("Delete Blog with Id: " + blogId);

            collection.deleteOne(eq("_id", new ObjectId(blogId)));

            System.out.println("Success Delete it! Sending as a response success delete with Id" + blogId);
            responseObserver.onNext(
                    DeleteBlogResponse.newBuilder()
                            .setBlogId(blogId)
                            .build()
            );
            responseObserver.onCompleted();
        }

    }

    private Blog documentToBlog(Document document) {
        return Blog.newBuilder()
                .setAuthorId(document.getString("author_id"))
                .setTitle(document.getString("title"))
                .setContent(document.getString("content"))
                .setId(document.getObjectId("_id").toString())
                .build();

    }
}
