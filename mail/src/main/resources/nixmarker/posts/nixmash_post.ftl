<div class="post nixmash-post">
<#if  post.postImage??>
    <img alt="" src="${post.postImage}" class="thumbnail-image"/>
</#if>
    <h2><a target="_blank" href="/post/${post.postName}">${post.postTitle}</a></h2>
    <div class="post-content">${post.postContent}</div>
    <div class="post-footer">
    <#include "includes/footer.ftl">
    </div>
    <div class="nixmash-tag"><a href="http://nixmash.com">
        <img src="/images/posts/nixmashtag.png" alt=""/></a></div>
</div>
