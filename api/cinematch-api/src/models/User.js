const mongoose = require('mongoose');

const userSchema = new mongoose.Schema({
  FirstName: { type: String, required: true },
  LastName: { type: String, required: true },
  Login: { type: String, required: true, unique: true },
  Password: { type: String, required: true },
  Services: { type: [String], default: [] },
  FavGenre: { type: [String], default: [] },
  NewUser: { type: Boolean, default: true },
  EmailVerified: { type: Boolean, default: false }
}, { 
  collection: 'Users',
  versionKey: false
});

module.exports = mongoose.model('User', userSchema);
