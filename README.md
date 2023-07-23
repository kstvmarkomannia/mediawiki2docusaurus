MediaWiki2Docusaurus
====================

This is a migration tool to migrate MediaWiki to Docusaurus. 

It was developed to migrate a MediaWiki 1.35.2 instance with language German to Docusaurus 2 at https://www.markomannenwiki.de automatically. The migration does not require access to the MediaWiki MySQL database, but instead crawls the MediaWiki website and downloads all pages and images. The pages are then converted to Markdown and the images are copied to the Docusaurus project.

To execute the tool, first configure the URL in `org.markomannia.Config` and then run `org.markomannia.Main`. 

The cleansing code is adjusted to German MediaWiki labels such as "Nächste Seite" for paginating pages. For other languages the labels thus need to be adjusted directly in the Java code.

💫 **Star** if you like our work.
