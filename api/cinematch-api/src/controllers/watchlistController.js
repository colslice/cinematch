const WatchList = require('../models/WatchList');

exports.addToWatchList = async (req, res) => {
  try {
    const item = await WatchList.create(req.body);
    res.status(201).json(item);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

exports.getUserWatchList = async (req, res) => {
  try {
    const list = await WatchList.find({ userId: req.params.userId });
    res.json(list);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

exports.updateWatchListStatus = async (req, res) => {
  try {
    const item = await WatchList.findByIdAndUpdate(
      req.params.id, 
      { status: req.body.status }, 
      { new: true }
    );
    res.json(item);
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};

exports.removeFromWatchList = async (req, res) => {
  try {
    await WatchList.findByIdAndDelete(req.params.id);
    res.json({ message: 'Removed from watchlist' });
  } catch (error) {
    res.status(500).json({ error: error.message });
  }
};