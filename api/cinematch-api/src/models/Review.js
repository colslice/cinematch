const mongoose = require('mongoose');

const reviewSchema = new mongoose.Schema({
  userId: { type: String, required: true },
  movieId: { type: String, required: true },
  rating: { type: Number, required: true },
  CreatedAt: { type: Date, default: Date.now }
}, { 
  collection: 'Reviews',
  versionKey: false
});

module.exports = mongoose.model('Review', reviewSchema);