const User = require('../models/User');

exports.signup = async (req, res) => {
  try {
    const { FirstName, LastName, Login, firebaseUid } = req.body;

    const existingUser = await User.findOne({ Login });
    if (existingUser) {
      return res.status(400).json({ error: 'Login already exists' });
    }

    const user = await User.create({
      FirstName,
      LastName,
      Login,
      Password: firebaseUid,
      Services: [],
      FavGenre: [],
      NewUser: true
    });

    res.status(201).json({
      _id: user._id,
      FirstName: user.FirstName,
      LastName: user.LastName,
      Login: user.Login,
      Services: user.Services,
      FavGenre: user.FavGenre,
      NewUser: user.NewUser
    });
  } catch (error) {
    res.status(500).json({ error: 'Server error', details: error.message });
  }
};

exports.login = async (req, res) => {
  try {
    const { Login } = req.body;

    const user = await User.findOne({ Login });
    if (!user) {
      return res.status(404).json({ error: 'User not found' });
    }

    res.json({
      _id: user._id,
      FirstName: user.FirstName,
      LastName: user.LastName,
      Login: user.Login,
      Services: user.Services,
      FavGenre: user.FavGenre,
      NewUser: user.NewUser
    });
  } catch (error) {
    res.status(500).json({ error: 'Server error', details: error.message });
  }
};
