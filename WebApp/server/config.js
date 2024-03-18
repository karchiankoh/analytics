const config = {
  db: {
    /* don't expose password or any sensitive info, done only for demo */
    host: "localhost",
    user: "user",
    password: "password",
    database: "gbi",
    connectTimeout: 60000,
    decimalNumbers: true,
    multipleStatements: true
  },
  // listPerPage: 10,
};
module.exports = config;