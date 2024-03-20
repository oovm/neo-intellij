<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# Neo Changelog

## [0.4.1]
### Fixed
- Fix color parse error([#9](https://github.com/oovm/neo-intellij/issues/9))
- Better `test` select color([#7](https://github.com/oovm/neo-intellij/issues/7))

## [0.4.0]
### New
- Customized `wat`(Web Assembly Text) color matching
- Customized `wit`(Wasm Interface Type) color matching
### Changed
- Update to 2024.1 version `(241.*)`
- Update gradle to 8.5.1

## [0.3.2]
### Changed
- Update to 2023.3 version `(233.*)`
- Update gradle to 8.3
### Fixed
- Prevent refactoring selections from being obscured([#5](https://github.com/oovm/neo-intellij/issues/8))

## [0.3.1]
### Changed
- Update to 2023.2 version `(232.*)`
- Update gradle to 8.1.1
### Fixed
- Fix missing selection background color([#5](https://github.com/oovm/neo-intellij/issues/5))

## [0.3.0]
### Changed
- Update to 2023.1 version `(231.*)`
- Update runtime to jvm17
- Update gradle to 7.5.1

## [0.2.3]
### Changed
- Update to 2022.2 version `(222.*)`

## [0.2.2]
### Changed
- Update to 2022 version `(221.*)`
### Fixed
- Wrong compatibility range set when publishing
## [0.2.1]
### Changed
- Improve readability of test files([#2](https://github.com/oovm/neo-intellij/issues/2))
- Update to 2022 version `(220.*)`

## [0.2.0]
### Fixed
- Fix the problem that bnf keywords are not highlighted ([#1](https://github.com/oovm/neo-intellij/pull/1))

### Changed

- XML & HTML elements use <span style="color:#E5C17C">Structure Color</span>
- XML & HTML attributes use <span style="color:#F07178">Field Color</span>

By default, html element is a keyword, that has a certain truth.

But considering the consistency with jsx and tsx, it is more appropriate to treat it as a structure

## [0.1.0]
### Added
- Neo Light: Based on atom one light
- Neo Night: Based on atom one dark
