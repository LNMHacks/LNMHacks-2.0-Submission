/**
 * Created by hackbansu on 4/4/17.
 */

const mysql = require('mysql');
const database = require('./database.js');
const pool = database.pool;


//get parking availability via identity(object) and call done
function get(identity, done) {
    let sql = "SELECT * FROM parking_status ";

    pool.getConnection(function (err, connection) {
        if (err) throw err;

        connection.query(sql, function (err, result, fields) {
            connection.release();
            if (err) {
                return done(err);
            }
            return done(null, result, fields);
        });
    });
}


//update parking availability with updates(object) via identity(object) and call done
//identity: {name}, updates: {goingOut, vehicleType}
function updateAvail(identity, updates, done) {
    let vehColumn = updates.vehicleType + "_avail";
    let sign = "-"; // if vehicle comes inside parking
    if (updates.goingOut === 1) {   // if vehicle goes out of parking
        sign = "+";
    }

    let sql = "UPDATE parking_status SET " + vehColumn + " = " + vehColumn + " " + sign + " 1 " +
        "WHERE name = ?";

    pool.getConnection(function (err, connection) {
        if (err) throw err;

        connection.query(sql, [identity.name], function (err, result, fields) {
            connection.release();
            if (err) {
                return done(err);
            }
            return done(null, result, fields);
        });
    });
}


module.exports = {
    updateAvail,
    get
};