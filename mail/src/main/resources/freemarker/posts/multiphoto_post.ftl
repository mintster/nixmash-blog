<#-- @ftlvariable name="post" type="com.nixmash.blog.jpa.model.Post" -->

<div class="post note">
    <h2><a target="_blank"
           href="/posts/post/${post.postName}">${post.postTitle}</a></h2>
    <div class="post-content">${post.postContent}</div>
    <div class="post-photos">
    <#if  post.postImages??>
        <#list post.postImages as image>
            <img src="${image.url}${image.thumbnailFilename}" alt=""
                 class="post-photo-thumbnail"/>
        </#list>
    </#if>
    </div>
    <div class="post-footer">
    <#include "includes/footer.ftl">
    </div>
</div>
