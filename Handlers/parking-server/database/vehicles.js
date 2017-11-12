/**
 * Created by hackbansu on 4/4/17.
 */

const mysql = require('mysql');
const database = require('./database.js');
const pool = database.pool;

//insert new entry via identity(object) and call done
//identity: {vehNum, name}
function insert(identity, done) {
    let sql = "INSERT INTO vehicles SET ? ";

    pool.getConnection(function (err, connection) {
        if (err) throw err;

        connection.query(sql, [identity], function (err, result, fields) {
            connection.release();
            if (err) {
                return done(err);
            }
            return done(null, result, fields);
        });
    });
}

//delete entry via identity(object) and call done
//identity: {vehNum, name}
function remove(identity, done) {
    let sql = "DELETE FROM vehicles WHERE veh_num = ? ";

    pool.getConnection(function (err, connection) {
        if (err) throw err;

        connection.query(sql, [identity.veh_num], function (err, result, fields) {
            connection.release();
            if (err) {
                return done(err);
            }
            return done(null, result, fields);
        });
    });
}

//get entry via identity(object) and call done
//identity: {vehNum, name}
function get(identity, done) {
    let sql = "SELECT * FROM vehicles WHERE veh_num = ? ";

    pool.getConnection(function (err, connection) {
        if (err) throw err;

        connection.query(sql, [identity.veh_num], function (err, result, fields) {
            connection.release();
            if (err) {
                return done(err);
            }
            return done(null, result, fields);
        });
    });
}



module.exports = {
    insert,
    remove,
    get,
};