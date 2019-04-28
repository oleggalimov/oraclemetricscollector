const path = require ('path');

module.exports = {
    mode:"development",
    entry :"./src/index.tsx",
    output :{
        path: path.resolve (__dirname, "D:\\work\\oraclemetricscollector\\src\\main\\webapp\\WEB-INF\\static\\js"),
        filename: "bundle.js",
        publicPath: "/public/",

    },
    module: {
        rules: [
            {
                test: /\.(ts|tsx)$/,
                loader: "ts-loader"
            },
            {
                test:/\.css$/,
                use: [
                    'style-loader',
                    'css-loader'
                ]
                // loader: 'style-loader!css-loader'
            }
        ]
    },
    resolve: {
        extensions: [".ts", ".tsx", ".js", ".jsx",".json",".css"]
    },
    devtool:"source\-map",
    devServer: {
        contentBase: path.join (__dirname, "public"),
        port:9000,
        //hot:true,
        https:false,
        historyApiFallback:true
    }
}