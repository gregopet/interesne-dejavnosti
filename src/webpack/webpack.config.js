const path = require('path');
const MiniCssExtractPlugin = require('mini-css-extract-plugin');
const HtmlWebpackPlugin = require('html-webpack-plugin');
const { CleanWebpackPlugin } = require('clean-webpack-plugin');
const webpack = require('webpack');
const WebpackShellPlugin = require('webpack-shell-plugin');

module.exports = (env, argv) => {
  const devMode = argv.mode !== 'production';
  return {
    entry: {
      pupil: ['whatwg-fetch', './src/app.js'],
      admin: './src/pupilList.js',
    },
    plugins: [
      new CleanWebpackPlugin(),
      new HtmlWebpackPlugin({
        filename: '../../views/generated/PupilScripts.rocker.html',
        template: './rockerTemplate.html',
        inject: false,
        chunks: ['pupil']
      }),
      new HtmlWebpackPlugin({
        filename: '../../views/generated/AdminScripts.rocker.html',
        template: './rockerTemplate.html',
        inject: false,
        chunks: ['admin']
      }),
      new MiniCssExtractPlugin({
        filename: devMode ? '[name].css' : '[name].[hash].css',
      }),
      new webpack.ProvidePlugin({
        Promise: 'es6-promise'
      }),
      new WebpackShellPlugin({
        dev: false, // run script every time a build finishes, as when using 'watch'
        onBuildEnd: devMode ? ['cp -r ../ratpack/static ../../build/resources/main'] : []
      }),
    ],
    devtool: devMode ? 'inline-source-map' : 'source-map',
    module: {
      rules: [
        {
          test: /\.tsx?$/,
          use: 'ts-loader',
          exclude: /node_modules/,
        },
        {
          test: /\.(css|scss|sass)$/i,
          use: [
            // Creates `style` nodes from JS strings
            MiniCssExtractPlugin.loader,
            // Translates CSS into CommonJS
            'css-loader',
            // Compiles Sass to CSS
            'sass-loader',
          ],
        },{
          test: /\.(png|jpe?g|gif|svg)$/i,
          loader: 'file-loader',
        },
      ],
    },
    resolve: {
      extensions: [ '.tsx', '.ts', '.js' ],
      alias: {
        vue: 'vue/dist/vue.esm.js',
      },
    },
    output: {
      filename: devMode ? '[name].js' : '[name].[contenthash].js',
      path: path.resolve(__dirname, '../ratpack/static/'),
      publicPath: "/static/",
    },
  };
}