require('dotenv').config();
const express = require('express');
const mongoose = require('mongoose');
const cors = require('cors');

const authRoutes = require('./src/routes/authRoutes');
const userRoutes = require('./src/routes/userRoutes');
const reviewRoutes = require('./src/routes/reviewRoutes');
const watchlistRoutes = require('./src/routes/watchlistRoutes');
const recommendRoutes = require('./src/routes/recommendRoutes');
const app = express();

// Middleware
app.use(cors());
app.use(express.json());

// Mount Routes
app.use('/api/recommend', recommendRoutes);
app.use('/api/auth', authRoutes);
app.use('/api/users', userRoutes);
app.use('/api/reviews', reviewRoutes);
app.use('/api/watchlist', watchlistRoutes);

// Connect to MongoDB Atlas
mongoose.connect(process.env.MONGO_URI)
  .then(() => console.log('Successfully connected to the COP4331 database on Atlas'))
  .catch((err) => console.error('Atlas connection error:', err));

// Start the Server
const PORT = process.env.PORT || 5000;
app.listen(PORT, () => console.log(`Server running on port ${PORT}`));
