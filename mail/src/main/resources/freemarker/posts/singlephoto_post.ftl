<#-- @ftlvariable name="post" type="com.nixmash.blog.jpa.model.Post" -->
<div class="post note">
    <h2><a target="_blank"
           href="/post/${post.postName}">${post.postTitle}</a></h2>
<#if  post.singleImage??>
    <div class="post-single-photo">
        <img src="${post.singleImage.url}${post.singleImage.newFilename}" alt="" class="post-photo-single" />
    </div>
</#if>
    <div class="post-content">${post.postContent}</div>
    <div class="post-footer">
    <#include "includes/footer.ftl">
    </div>
</div>
