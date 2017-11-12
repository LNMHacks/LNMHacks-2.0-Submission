const mysql = require('mysql');

const dbPoolConf = {
    connectionLimit: 100,
    host: 'localhost',
    user: 'root',
    password: 'bansal26',
    database: 'parking_status',
};
const pool = mysql.createPool(dbPoolConf);
pool.on('acquire', function (connection) {
    console.log('Connection %d acquired', connection.threadId);
});
pool.on('connection', function (connection) {
    connection.query('SET SESSION auto_increment_increment=1')
});
pool.on('enqueue', function () {
    console.log('Waiting for available connections slot');
});
pool.on('release', function (connection) {
    console.log('Connection %d released', connection.threadId);
});

//exporting pool
module.exports.pool = pool;

//exporting things
module.exports = {
    parkingStatusTable: require('./parkingStatus.js'),
    vehiclesTable: require('./vehicles.js'),
};
