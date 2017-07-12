package com.tripas.test1.test1.svg;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;

import com.larvalabs.svgandroid.SVG;
import com.larvalabs.svgandroid.SVGParser;

//import com.larvalabs.svgandroid.SVGBuilder;

public class SVGImage {

    private static ImageView imageView;
    private Activity activity;
    private SVG svg;
    private int xmlLayoutId;
    private int drawableId;


    public SVGImage(Activity activity, int layoutId, int drawableId) {
        imageView = (ImageView) activity.findViewById(layoutId);
        svg = SVGParser.getSVGFromResource(activity.getResources(), drawableId);
        //Needed because of image accelaration in some devices such as samsung
        imageView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        imageView.setImageDrawable(svg.createPictureDrawable());
    }


//    public SVGImage(Activity activity, ImageView imageView, int drawableId) {
//        // Load and parse a SVG
//        svg = new SVGBuilder()
//                .readFromResource(activity.getResources(), drawableId) // if svg in res/raw
////                .readFromAsset(getAssets(), "somePicture.svg")           // if svg in assets
//                // .setWhiteMode(true) // draw fills in white, doesn't draw strokes
//                // .setColorSwap(0xFF008800, 0xFF33AAFF) // swap a single colour
//                // .setColorFilter(filter) // run through a colour filter
//                // .set[Stroke|Fill]ColorFilter(filter) // apply a colour filter to only the stroke or fill
//                .build();
//
//        // Draw onto a canvas
//        Canvas canvas = new Canvas();
//        canvas.drawPicture(svg.getPicture());
//
//        // Turn into a drawable
//        Drawable drawable = svg.getDrawable();
//        drawable.draw(canvas);
//        imageView.setImageDrawable(drawable);
//    }
//
//    public void drawPicture(Canvas canvas) {
//        // Draw onto a canvas
//        if (canvas != null
//                && svg != null) {
//            canvas.drawPicture(svg.getPicture());
//        }
//}
}