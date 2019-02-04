'use strict';
const functions = require('firebase-functions');

const MAX_USERS = 10;

exports.truncate = functions.database.ref('/User').onWrite((change) => {
  const parentRef = change.after.ref;
  const snapshot = change.after

  if (snapshot.numChildren() >= MAX_USERS) {
    let childCount = 0;
    const updates = {};
    snapshot.forEach((child) => {
      if (++childCount <= snapshot.numChildren() - MAX_USERS) {
        updates[child.key] = null;
      }
    });
    return parentRef.update(updates);
  }
  return null;
});
