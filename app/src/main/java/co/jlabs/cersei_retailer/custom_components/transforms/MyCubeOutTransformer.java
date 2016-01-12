/*
 * Copyright 2014 Toxic Bakery
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.jlabs.cersei_retailer.custom_components.transforms;

import android.view.View;

public class MyCubeOutTransformer extends ABaseTransformer {

	private static final float MIN_SCALE = 0.85f;
	private static final float MAX_SCALE = 1f;
    private static final float INT_POS = 0.05882353f;

	@Override
	protected void onTransform(View view, float position) {
        //if (position >= -1 || position <= 1) {
        position=position-INT_POS;
        if (position >= -1 || position <= 1) {
            final float height = view.getHeight();
            final float width = view.getWidth();
            float scaleFactor = Math.max(MIN_SCALE, 1 - Math.abs(position));

            final float vertMargin = height * (MAX_SCALE - scaleFactor) / 2;
            float horzMargin = width * (MAX_SCALE - scaleFactor) / 2;

            // Center vertically
            view.setPivotY(0.5f * height);
            view.setPivotX(0.5f * width);

//            Log.i("Hai ", "margin = [" + view.getTranslationX() + "]");
            if (position < 0) {
                    view.setTranslationX((horzMargin - vertMargin / 2));
            } else {
                    view.setTranslationX((-horzMargin + vertMargin / 2));
            }

            view.setScaleX(scaleFactor);
            view.setScaleY(scaleFactor);

        }
    }




//
//    private static final float MIN_SCALE = 0.85f;
//    private static final float MAX_SCALE = 0.91f;
//
//    @Override
//    protected void onTransform(View view, float position) {
//        //if (position >= -1 || position <= 1) {
//        if (position >= -1 || position <= 1) {
//            final float height = view.getHeight();
//            final float width = view.getWidth();
//            float scaleFactor = Math.max(MIN_SCALE, MAX_SCALE - Math.abs(position));
//
//            final float vertMargin = height * (MAX_SCALE - scaleFactor) / 2;
//            float horzMargin = width * (MAX_SCALE - scaleFactor) / 2;
//
//            // Center vertically
//            view.setPivotY(0.5f * height);
//            view.setPivotX(0.5f * width);
//
////            Log.i("Hai ", "margin = [" + view.getTranslationX() + "]");
//            if (position < 0) {
//                view.setTranslationX((horzMargin - vertMargin / 2));
//            } else {
//                view.setTranslationX((-horzMargin + vertMargin / 2));
//            }
//
//            view.setScaleX(scaleFactor);
//            view.setScaleY(scaleFactor);
//
//        }
//    }



}
