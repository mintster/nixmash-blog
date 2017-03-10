<#-- @ftlvariable name="baseurl" type="java.lang.String" -->
<#-- @ftlvariable name="post" type="com.nixmash.blog.jpa.model.Post" -->

<div>
${post.postContent}
</div>

<#if  post.postImages??>
<div>
    <#list post.postImages as image>
        <img src="${baseurl}${image.url}${image.thumbnailFilename}" alt=""
             style="max-width: 100%; width: 800px; max-height: 100%;"/>
    </#list>
</div>
</#if>

<#if  post.singleImage??>
<div>
    <img src="${baseurl}${post.singleImage.url}${post.singleImage.newFilename}" alt=""
         style="margin-right: 15px; vertical-align: top;" />
</div>
</#if>