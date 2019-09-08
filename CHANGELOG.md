# Changelog

## [0.5.2] - [0.7.2]

Note: These updates were part of rapid development in par with an application, this caused several issues with some version, but allowed for much faster development.
This brings the library close to 1.0 release after which every new release will be require testing before it's released.

## [0.7.2] - 08-09-2019

- *BREAKING* Fixed oversight which made it significantly harder to implement callbacks inside adapter. Moved callbacks out of constructor.
- *BREAKING* PriorityWrap is now initialized using Companion method create to disallow creation of null values, which is used only during initialization to get class of the generic instance.

## [0.7.0] - 08-09-2019

- Add BaseSortAdapter and BaseWrapSortAdapter as base sorting adapters
- **BREAKING** Rewritten SortableAdapter to use BaseWrapSortAdapter and renamed it to PrioritySortAdapter
- Changed project style

## [0.6.1] - 07-09-2019

- Add predicate interfaces
- **BREAKING** Moved interfaces to new packages
- MultiTypeAdapter ViewHolder can now be overridden
- BaseRecyclerAdapter and SortableAdapter implement new predicate interfaces

## [0.5.3] - 06-09-2019

- Add updateAt to MutableAdapter to allow (and encourage) proper usage of immutable objects

## [0.5.2] - 05-09-2019

- **BREAKING** Replaced CardItemDecoration with much more powerful MarginDecoration
- **BREAKING** Refactored library structure
- Add interfaces to unify adapter behaviours
- Add BaseRecyclerAdapter that does basic data manipulation (You still need to implement bind logic)
- Add MultiTypeAdapter allowing safe use of multiple view types
- Updated dependencies

## [0.4.2] - 29-06-2019

- Added removeAt method to SortableAdapter

## [0.4.1] - 14-06-2019

- Refactoring (There might be some compatibility issues with named arguments)
- Updated dependencies
- Public API (functions, classes, interfaces, objects) should now be properly documented

## [0.4.0] - 25-04-2019

- Renamed to Recycler library
- Add new SortableAdapter which can be used more generally
- API changes
- Refactoring

## [0.3.2] - 03-04-2019

- Add documentation
- TableCard now exposes all collections as immutable (Removing is still not supported)
- Refactoring

## [0.3.1] - 30-03-2019

- Fixed title not drawing
- Slight changes to API
- Refactoring


## [0.3.0] - 27-03-2019

Release 0.3.0 has completely rewritten API and is incompatible with 0.2.0. However it can be used with RecyclerView and be more versatile.

- Rewritten to use RecyclerView instead of ListView
- Cards are now more versatile and can have custom inside Views
- Renamed AppendBehavior to AppendBehaviour
- AppendBehaviour is now enum instead of integer with restrictions
- Renamed library from Table to CardList to properly reflect new changes
- Updated to AndroidX
- Updated dependencies


## [0.2.0] - 10-05-2018

- Add additional documentation
- Rewritten to Kotlin

## [0.1.2] - 23-11-2017

- Removed unused context from Table constructor
- Fixed divider color sometimes not being loaded

## [0.1.1] - 23-11-2017

- Divider can now change color based on card's background

## [0.1.0] - 23-11-2017

- Initial release
