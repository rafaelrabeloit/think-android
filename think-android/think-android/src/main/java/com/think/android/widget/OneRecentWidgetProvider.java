package com.think.android.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;
import android.widget.RemoteViews;
import br.com.think.model.Quote;
import br.com.think.model.UserQuote;

import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EReceiver;
import com.think.android.R;
import com.think.android.data.DataPersistence;
import com.think.android.preference.UserConfiguration;

/**
 * Vou fazer o seguinte. A widget vai ler e mostrar a entrada mais recente das UserQuotes. Pra isso vou precisar da persistencia. O serviço só vai fazer o request e gravar no banco, por enquanto.
 * @author Rafael
 *
 */
@EReceiver
public class OneRecentWidgetProvider extends AppWidgetProvider {

	@Bean
	UserConfiguration config;
	
	@Bean
	DataPersistence persist;
	
	@Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
    	
        final int N = appWidgetIds.length;
        
        UserQuote userQuote = persist.queryLastUserQuote();
        Quote quote = null;
        
        if(userQuote!=null)
        	quote = (Quote) userQuote.getRootQuote().getQuotes().toArray()[0];

        // Perform this loop procedure for each App Widget that belongs to this provider
        for (int i=0; i<N; i++) {
            int appWidgetId = appWidgetIds[i];
            
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_onerecent);
            
            views.setInt(R.id.layout_widget, "setBackgroundResource", config.getBackgroundWidgetColor());

            int color = context.getResources().getColor(config.getForegroundColor());
            
            if(userQuote!=null){
            	views.setViewVisibility(R.id.layout_quote_item, View.VISIBLE);
            	views.setViewVisibility(R.id.text_error_widget, View.GONE);
            	
	            views.setImageViewResource(R.id.image_quote_left_mark, config.getQuoteMarkLeftImage()); 
	            
	            views.setTextColor(R.id.text_quote_preview, color );
	            views.setTextViewText(R.id.text_quote_preview, quote.getText() );
	            
	            views.setImageViewBitmap(R.id.text_category_description, getFontBitmap(context, quote.getCategoryDescription(), color, 11));
	            if( quote.getReferenceDescription() != null ) views.setImageViewBitmap(R.id.text_reference_description, getFontBitmap(context, quote.getReferenceDescription(), color, 11));
	            views.setImageViewBitmap(R.id.text_author_fullName, getFontBitmap(context, quote.getAuthorFullname(), color, 11));
            }
            else{

            	views.setViewVisibility(R.id.layout_quote_item, View.GONE);
            	views.setViewVisibility(R.id.text_error_widget, View.VISIBLE);

	            views.setTextColor(R.id.text_error_widget, color );
            	
            }
            
            // Tell the AppWidgetManager to perform an update on the current app widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        }  
    }
	
	public static Bitmap getFontBitmap(Context context, String text, int color, float fontSizeSP) {
	    int fontSizePX = convertDiptoPix(context, fontSizeSP);
	    int pad = (fontSizePX / 9);
	    Paint paint = new Paint();
	    Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/SEGOESC.TTF");
	    paint.setAntiAlias(true);
	    paint.setTypeface(typeface);
	    paint.setColor(color);
	    paint.setTextSize(fontSizePX);

	    int textWidth = (int) (paint.measureText(text) + pad * 2);
	    int height = (int) (fontSizePX / 0.75);
	    Bitmap bitmap = Bitmap.createBitmap(textWidth, height, Bitmap.Config.ARGB_4444);
	    Canvas canvas = new Canvas(bitmap);
	    float xOriginal = pad;
	    canvas.drawText(text, xOriginal, fontSizePX, paint);
	    return bitmap;
	}

	public static int convertDiptoPix(Context context, float dip) {
	    int value = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
	    return value;
	}
}
