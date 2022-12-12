# Mindee Java API Library Changelog

## v2.0.0 - 2022-12-12

* :boom: Document (endpoints) are now versioned, providing better backward-compatible support.
* :sparkles: New PDF cut system, which allows specifying exactly which pages to keep or remove.
* :sparkles: PDF documents are no longer cut by default, use the pageOptions parameter in the parse method.
* :sparkles: Add support fot Receipt v4
* :recycle: Improve the internal de-serialization process

## v1.1.1 - 2022-10-10

* :arrow_up: fix for Uncontrolled Resource Consumption in Jackson-databind

## v1.1.0 - 2022-10-03

* :sparkles: custom document is handled by sub objects (#21)

## v1.0.0 - 2022-09-21

* :tada: First official release!
