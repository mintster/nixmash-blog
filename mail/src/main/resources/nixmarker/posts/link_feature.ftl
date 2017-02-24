<div class="post link-feature">
    <h2><a href="/post/${post.postName}">${post.postTitle}</a></h2>
    <div class="post-content">${post.postContent}</div>
<#if  post.postImage??>
    <img alt="" src="${post.postImage}" class="feature-image"/>
</#if>
    <div class="post-footer">
    <#include "includes/footer.ftl">
    </div>

</div>
