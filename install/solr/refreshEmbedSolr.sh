#!/bin/bash 


# ---------------------------------------------------------------------- */
# COPY SOLR HTTP  INDEX TO EMBEDDED COLLECTION
# ---------------------------------------------------------------------- */

# NOTES ------------------------------------------------------------- */

# 1) Solr Version 5.5.2
# 2) Configure local Http Solr Server: 
#
#    Example used is http://solr/nixmashspring. Can be http://localhost:8983/solr/collection1 or whatever
#
# 3) This script will copy the HTTP Collection DATA directory to the embedded server
#    
#
# ------------------------------------------------------------------ */

# VARIABLES ------------------------------------------------------------- */

# 1) HTTP_COLLECTION - File Path of Http Default Collection
# 2) EMBEDDED_COLLECTION - File Path of Embedded Collection
#
# ------------------------------------------------------------------ */

HTTP_COLLECTION=/ubuntuland/utils/solr-5.5.2/server/solr/nixmash
EMBEDDED_COLLECTION=/home/daveburke/web/solr-5.5.2/nixmash

# Delete Embedded Collection and Copy from Http --------------------- */

rm -rf $EMBEDDED_COLLECTION/data
cp -r $HTTP_COLLECTION/data/ $EMBEDDED_COLLECTION/data/


