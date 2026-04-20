const User = require('../models/User');

exports.updateGenres = async (req, res) => {
    try {
        const user = await User.findByIdAndUpdate (
            req.params.id,
            { FavGenre : req.body.favGenres } ,
            { new: true }
        );
        res.json(user.FavGenre);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

exports.getGenres = async (req, res) => {
    try {
        const user = await User.findById(req.params.id).select('FavGenre');
        res.json(user.FavGenre);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
}

exports.updateServices = async (req, res) => {
    try {
        const user = await User.findByIdAndUpdate (
            req.params.id,
            { Services : req.body.services },
            { new: true }
        );
        res.json(user.Services);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
};

exports.getServices = async (req, res) => {
    try {
        const user = await User.findById(req.params.id).select('Services');
        res.json(user.Services);
    } catch (error) {
        res.status(500).json({ error: error.message });
    }
}

exports.addService = async (req, res) => {
  try {
    const user = await User.findByIdAndUpdate(
      req.params.id,
      { $addToSet: { Services: req.body.service } }, // Prevents duplicates
      { new: true }
    );
    res.json(user.Services);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

exports.removeService = async (req, res) => {
  try {
    const user = await User.findByIdAndUpdate(
      req.params.id,
      { $pull: { Services: req.body.service } },
      { new: true }
    );
    res.json(user.Services);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

exports.addGenre = async (req, res) => {
  try {
    const user = await User.findByIdAndUpdate(
      req.params.id,
      { $addToSet: { FavGenre: req.body.genre } },
      { new: true }
    );
    res.json(user.FavGenre);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

exports.removeGenre = async (req, res) => {
  try {
    const user = await User.findByIdAndUpdate(
      req.params.id,
      { $pull: { FavGenre: req.body.genre } },
      { new: true }
    );
    res.json(user.FavGenre);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

exports.toggleField = async (req, res) => {
  try {
    const { id, field } = req.params;
    const allowedFields = ['NewUser', 'EmailVerified'];

    if (!allowedFields.includes(field)) {
      return res.status(400).json({ error: 'Invalid field to toggle' });
    }

    const user = await User.findById(id);
    if (!user) {
      return res.status(404).json({ error: 'User not found' });
    }

    user[field] = !user[field];
    await user.save();

    res.json({ [field]: user[field] });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};
