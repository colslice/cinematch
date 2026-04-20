const mongoose = require('mongoose');

const watchListSchema = new mongoose.Schema({
  userId: { type: String, required: true },
  movieId: { type: String, required: true },
  status: { type: String, required: true },
  addedAt: { type: Date, default: Date.now }
}, { 
  collection: 'WatchList',
  versionKey: false
});

module.exports = mongoose.model('WatchList', watchListSchema);