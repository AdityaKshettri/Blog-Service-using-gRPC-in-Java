// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: blog.proto

package com.proto.blog;

public interface CreateBlogResponseOrBuilder extends
    // @@protoc_insertion_point(interface_extends:blog.CreateBlogResponse)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <pre>
   * id should correspond to the one in db
   * </pre>
   *
   * <code>.blog.Blog blog = 1;</code>
   * @return Whether the blog field is set.
   */
  boolean hasBlog();
  /**
   * <pre>
   * id should correspond to the one in db
   * </pre>
   *
   * <code>.blog.Blog blog = 1;</code>
   * @return The blog.
   */
  com.proto.blog.Blog getBlog();
  /**
   * <pre>
   * id should correspond to the one in db
   * </pre>
   *
   * <code>.blog.Blog blog = 1;</code>
   */
  com.proto.blog.BlogOrBuilder getBlogOrBuilder();
}
