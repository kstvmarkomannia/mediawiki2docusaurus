MediaWiki2Docusaurus
====================

This is a migration tool to migrate MediaWiki to Docusaurus. 

It was developed to migrate a MediaWiki 1.35.2 instance with language German to Docusaurus 2 at https://www.markomannenwiki.de automatically. The migration does not require access to the database, but instead crawls the MediaWiki instance and downloads all pages and images. The pages are then converted to Markdown and the images are copied to the Docusaurus project.

To execute the tool, configure in `org.markomannia.Config` and run `org.markomannia.Main`.

💫 **Star** if you like our work.
