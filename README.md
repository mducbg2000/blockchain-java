# blockchain-java

Simple blockchain command line app written by Java with some basic concept but incomplete

## How to run

This project use Maven as package manager, so run this command in project's root folder to install dependencies

```bash
mvn install
```

You can build this project by maven. However, because it is incomplete, there may be some errors. Instead I wrote a few test cases for some functions placed in CliControllerTest.java, you can run each test case independently. Since this project is command line app so we can test by parsing arguments to entry points funtions.

## Completed ✓
- [x] Proof-of-Work
- [x] Block storage
- [x] Transactions
- [x] Cli interation

## Progressing
- [ ] Add address

## Todo
- [ ] Build a p2p network
