package com.questcompendium.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebStorage;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.questcompendium.R;
import com.questcompendium.AppGlobal;
import com.questcompendium.adapter.FacilityAdapter;
import com.questcompendium.model.Facility;
import com.questcompendium.utils.Common;
import com.questcompendium.utils.Constants;
import com.yarolegovich.discretescrollview.DiscreteScrollView;
import com.yarolegovich.discretescrollview.transform.ScaleTransformer;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class FacilityActivity extends BaseActivity implements DiscreteScrollView.ScrollStateChangeListener<FacilityAdapter.SimpleViewHolder>,
        DiscreteScrollView.OnItemChangedListener<FacilityAdapter.SimpleViewHolder> {

    @BindView(R.id.rvFacility)
    DiscreteScrollView moRvFacility;
    /* @BindView(R.id.llEmpty)
     LinearLayout moLlEmpty;*/
  /*  @BindView(R.id.tvMainTitle)
    TextView moTvMainTitle;
    @BindView(R.id.tvEmptyDesc)
    TextView moTvEmptyDesc;*/
    @BindView(R.id.tvToolbarTitle)
    TextView moTvToolbarTitle;
    @BindView(R.id.llBack)
    LinearLayout moLlBack;
    @BindView(R.id.ibNavigation)
    ImageView ibNavigation;
    @BindView(R.id.text_view)
    TextView mTextView;
    @BindView(R.id.ivMenu)
    ImageView ivMenu;
    @BindView(R.id.ivTitleImage)
    ImageView ivTitleImage;
    @BindView(R.id.ivBanner)
    ImageView moIvBanner;
    String fsHotelId, fsHotelMenuId, fsCatId;
    String fsCount;
    @BindView(R.id.webView)
    WebView webView;
    String fsTitle = "", banner = "", link = "";
    private float m_downX;
    Facility moFacility;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark, this.getTheme()));
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));
        }

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        setContentView(R.layout.activity_facility);

        if (getIntent() != null) {
            //  moImgUrl = getIntent().getStringExtra(Constants.IMAGE_URL);
            fsHotelMenuId = getIntent().getStringExtra(Constants.HOTEL_MENU_ID);
            fsHotelId = getIntent().getStringExtra(Constants.HOTEL_ID);
            fsCatId = getIntent().getStringExtra(Constants.CAT_ID);
            fsCount = getIntent().getStringExtra(Constants.COUNT);
            fsTitle = getIntent().getStringExtra(Constants.TITLE);
            banner = getIntent().getStringExtra(Constants.BANNER);
            link = getIntent().getStringExtra(Constants.LINK);


            //fsTitleImg = getIntent().getStringExtra(Constants.BACKGROUND_TITLE_IMAGE);
        }
        initialize();
    }

    private void initialize() {
        ButterKnife.bind(this);
        //.setVisibility(View.GONE);
        // moIbNavigation.setVisibility(View.GONE);
        //ibNavigation.setVisibility(View.VISIBLE);
        // moTvBackText.setText("Back");
        //moIbNavigation.setImageDrawable(getResources().getDrawable(R.drawable.ic_arrow_back));
        ivMenu.setVisibility(View.VISIBLE);
        ivMenu.setImageDrawable(getResources().getDrawable(R.drawable.ic_home));
        ivMenu.setPadding(10, 10, 10, 10);
        moTvToolbarTitle.setVisibility(View.VISIBLE);

        moTvToolbarTitle.setText(fsTitle);
        moTvToolbarTitle.setEllipsize(TextUtils.TruncateAt.MARQUEE);
        moTvToolbarTitle.setSingleLine(true);
        moTvToolbarTitle.setMarqueeRepeatLimit(-1);
        moTvToolbarTitle.setSelected(true);
        moTvToolbarTitle.setHorizontallyScrolling(true);

        moLlBack.setVisibility(View.VISIBLE);

        ivTitleImage.setVisibility(View.GONE);

        if (!TextUtils.isEmpty(banner) && !TextUtils.isEmpty(link)) {
            setBanner(banner);
            setWebview(link);
            return;
        }
        if (!TextUtils.isEmpty(fsCount)) {
            if (fsCount.equalsIgnoreCase("1")) {
                getHotelFacility(false);
            } else {
                moRvFacility.setVisibility(View.VISIBLE);
                moRvFacility.setSlideOnFling(true);
                moRvFacility.addOnItemChangedListener(FacilityActivity.this);
                moRvFacility.addScrollStateChangeListener(FacilityActivity.this);
                moRvFacility.setItemTransformer(new ScaleTransformer.Builder()
                        .setMinScale(0.8f)
                        .build());
                getHotelFacility(true);
            }
        } else {
            getHotelFacility(false);
        }
    }

    private void setWebView(WebView foWebView, String fsUrl) {
        foWebView.getSettings().setAppCacheEnabled(false);
        foWebView.getSettings().setJavaScriptEnabled(true);
        foWebView.getSettings().setLoadWithOverviewMode(true);
        foWebView.getSettings().setUseWideViewPort(true);
        foWebView.getSettings().setDomStorageEnabled(true);
        foWebView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        foWebView.loadUrl(fsUrl);
    }

    private void clearCookie() {
        if (webView != null) {
            WebStorage.getInstance().deleteAllData();
            webView.setWebViewClient(null);
            webView.clearCache(true);
            webView.clearHistory();
            webView.removeAllViews();
            webView.clearSslPreferences();
        }
    }

    private void initWebView(String moRedirectUrl) {
        clearCookie();
        setWebView(webView, moRedirectUrl);
        webView.setVisibility(View.VISIBLE);

        Display display = getWindowManager().getDefaultDisplay();
        int width = display.getWidth();


        webView.evaluateJavascript("", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {

            }
        });

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                //  showProgress();
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("URL", url);
                String data = "<html><head><title>Example</title><meta name=\"viewport\"\"content=\"width="+width+", initial-scale=1 \" /></head>";

                data = data + "<body><center><img width=\""+width+"\" src=\""+url+"\" /></center></body></html>";


                if (url.contains("tel:")) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                    return true;
                } else if (url.startsWith("mailto")) {
                    handleMailToLink(url);
                    return true;
                } else if (url.startsWith("http") || url.startsWith("HTTP")) {
                    Common.openBrowser(FacilityActivity.this, url);
                    return true;
                } else {
                    // progressBar.setVisibility(view.VISIBLE);
                    view.loadData(data,"text/html",null);
                    return true;
                }
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
//                webView.evaluateJavascript(data, new ValueCallback<String>() {
//                    @Override
//                    public void onReceiveValue(String s) {
//
//                    }
//                });

                // hideProgress();
                view.clearHistory();
            }

            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                super.onReceivedError(view, request, error);
                //hideProgress();
                //finish();
            }
        });

        webView.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if (event.getPointerCount() > 1) {
                    //Multi touch detected
                    return true;
                }

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        // save the x
                        m_downX = event.getX();
                    }
                    break;

                    case MotionEvent.ACTION_MOVE:
                    case MotionEvent.ACTION_CANCEL:
                    case MotionEvent.ACTION_UP: {
                        // set x so that it doesn't move
                        event.setLocation(m_downX, event.getY());
                    }
                    break;
                }

                return false;
            }
        });
    }


    protected void handleMailToLink(String url) {
        // Initialize a new intent which action is send
        Intent intent = new Intent(Intent.ACTION_SENDTO);

        // For only email app handle this intent
        intent.setData(Uri.parse("mailto:"));

        // Empty the text view
        mTextView.setText("");

        // Extract the email address from mailto url
        String to = url.split("[:?]")[1];
        if (!TextUtils.isEmpty(to)) {
            String[] toArray = to.split(";");
            // Put the primary email addresses array into intent
            intent.putExtra(Intent.EXTRA_EMAIL, toArray);
            mTextView.append("TO : " + to);
        }

        // Extract the cc
        if (url.contains("cc=")) {
            String cc = url.split("cc=")[1];
            if (!TextUtils.isEmpty(cc)) {
                //cc = cc.split("&")[0];
                cc = cc.split("&")[0];
                String[] ccArray = cc.split(";");
                // Put the cc email addresses array into intent
                intent.putExtra(Intent.EXTRA_CC, ccArray);
                mTextView.append("\nCC : " + cc);
            }
        } else {
            mTextView.append("\n" + "No CC");
        }

        // Extract the bcc
        if (url.contains("bcc=")) {
            String bcc = url.split("bcc=")[1];
            if (!TextUtils.isEmpty(bcc)) {
                //cc = cc.split("&")[0];
                bcc = bcc.split("&")[0];
                String[] bccArray = bcc.split(";");
                // Put the bcc email addresses array into intent
                intent.putExtra(Intent.EXTRA_BCC, bccArray);
                mTextView.append("\nBCC : " + bcc);
            }
        } else {
            mTextView.append("\n" + "No BCC");
        }

        // Extract the subject
        if (url.contains("subject=")) {
            String subject = url.split("subject=")[1];
            if (!TextUtils.isEmpty(subject)) {
                subject = subject.split("&")[0];
                // Encode the subject
                try {
                    subject = URLDecoder.decode(subject, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                // Put the mail subject into intent
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                mTextView.append("\nSUBJECT : " + subject);
            }
        } else {
            mTextView.append("\n" + "No SUBJECT");
        }

        // Extract the body
        if (url.contains("body=")) {
            String body = url.split("body=")[1];
            if (!TextUtils.isEmpty(body)) {
                body = body.split("&")[0];
                // Encode the body text
                try {
                    body = URLDecoder.decode(body, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                // Put the mail body into intent
                intent.putExtra(Intent.EXTRA_TEXT, body);
                mTextView.append("\nBODY : " + body);
            }
        } else {
            mTextView.append("\n" + "No BODY");
        }

        // Email address not null or empty
        if (!TextUtils.isEmpty(to)) {
            if (intent.resolveActivity(getPackageManager()) != null) {
                // Finally, open the mail client activity
                startActivity(intent);
            } else {
                Toast.makeText(FacilityActivity.this, "No email client found.", Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    public void onCurrentItemChanged(@Nullable FacilityAdapter.SimpleViewHolder simpleViewHolder, int position) {
        if (simpleViewHolder != null) {
            try {
                if (moFacility != null && moFacility.getFoFacilityList().length > 0) {
                    initWebView(moFacility.getFoFacilityList()[position].getFsDescriptionLink());
                    setBanner(moFacility.getFoFacilityList()[position].getFsFacilityBanner());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            simpleViewHolder.showText();
        }
    }

    @Override
    public void onScrollStart(@NonNull FacilityAdapter.SimpleViewHolder simpleViewHolder, int i) {
        simpleViewHolder.hideText();
    }

    @Override
    public void onScrollEnd(@NonNull FacilityAdapter.SimpleViewHolder simpleViewHolder, int i) {

    }

    @Override
    public void onScroll(float v, int i, int i1, @Nullable FacilityAdapter.SimpleViewHolder simpleViewHolder, @Nullable FacilityAdapter.SimpleViewHolder t1) {

    }

    private void getHotelFacility(boolean isShowList) {
        if (!isNetworkConnected()) {
            toastMessage("No Internet Connection");
            return;
        }

        if (TextUtils.isEmpty(fsHotelId) || TextUtils.isEmpty(fsHotelMenuId)) {
            toastMessage("Hotel id is empty");
            return;
        }

        showProgress();

        Call<Facility> loFacilityResponse = AppGlobal.moAppService.getHotelFacility(fsHotelId, fsHotelMenuId, fsCatId);
        loFacilityResponse.enqueue(new Callback<Facility>() {

            @Override
            public void onResponse(Call<Facility> call, Response<Facility> foResponse) {
                hideProgress();
                Common.printReqRes(foResponse.body(), "GetFacility", Constants.API_RESPONSE);
                if (foResponse.isSuccessful()) {
                    try {
                        Log.d("TTT", "response body..." + foResponse.body());
                        moFacility = foResponse.body();
                        if (moFacility != null && moFacility.getFbIsSuccess().equalsIgnoreCase("true")) {
                            if (moFacility.getFoFacilityList() != null && moFacility.getFoFacilityList().length > 0) {
                                if (isShowList) {
                                    moRvFacility.setVisibility(View.VISIBLE);
                                    FacilityAdapter mHotelAdapter = new FacilityAdapter(FacilityActivity.this,
                                            moFacility.getFoFacilityList());
                                    moRvFacility.setAdapter(mHotelAdapter);
                                    moRvFacility.scrollToPosition(2);
                                    setBanner(moFacility.getFoFacilityList()[1].getFsFacilityBanner());
                                    setWebview(moFacility.getFoFacilityList()[1].getFsDescriptionLink());
                                } else {
                                    moRvFacility.setVisibility(View.GONE);
                                    try {
                                        setBanner(moFacility.getFoFacilityList()[0].getFsFacilityBanner());
                                        setWebview(moFacility.getFoFacilityList()[0].getFsDescriptionLink());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            } else {
                                toastMessage("No Result Found");
                                // setEmptyScreen();
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    String fsMessage = Common.getErrorMessage(null);
                    Common.showErrorDialog(FacilityActivity.this, fsMessage, false);
                }
            }

            @Override
            public void onFailure(Call<Facility> call, Throwable t) {
                hideProgress();
                Common.printReqRes(t, "GetFacility", Constants.API_ERROR);
                if (t instanceof IOException) {
                    Common.showErrorDialog(FacilityActivity.this, t.getMessage(), false);
                } else {
                    Common.showErrorDialog(FacilityActivity.this, t.getMessage(), false);
                }
            }
        });
    }

    private void setBanner(String fsBanner) {
        //  Log.d("TTT", "fsBanner..." + fsBanner);
        if (fsBanner != null && !fsBanner.isEmpty()) {
            moIvBanner.setVisibility(View.VISIBLE);
            Glide.with(this).
                    load(fsBanner).
                    placeholder(getResources().getDrawable(R.drawable.iv_placeholder)).
                    into(moIvBanner);
        } else {
            moIvBanner.setVisibility(View.GONE);
        }
    }

    private void setWebview(String url) {
        initWebView(url);
    }

    public void setEmptyScreen() {
        // moLlEmpty.setVisibility(View.VISIBLE);
        moRvFacility.setVisibility(View.GONE);
        //   moTvMainTitle.setText(getString(R.string.no_facility));
        // moTvEmptyDesc.setText(getString(R.string.no_facility_desc));
    }

    /* private Facility.FoFacilityList[] initializeItem() {
        Facility.FoFacilityList fontList = new Facility.FoFacilityList("Spa", "1", "www.wrpsolution.com");
        Facility.FoFacilityList fontList1 = new Facility.FoFacilityList("Colling", "1", "www.wrpsolution.com");
        Facility.FoFacilityList fontList2 = new Facility.FoFacilityList("Outdoor restaurant", "1", "www.wrpsolution.com");
        Facility.FoFacilityList fontList3 = new Facility.FoFacilityList("Poolside bar", "1", "www.wrpsolution.com");
        Facility.FoFacilityList fontList4 = new Facility.FoFacilityList("Swimming pool/ Jacuzzi", "1", "www.wrpsolution.com");
        Facility.FoFacilityList fontList5 = new Facility.FoFacilityList("Disable rooms & Interconnecting rooms", "1", "www.wrpsolution.com");
        Facility.FoFacilityList fontList6 = new Facility.FoFacilityList("24 Hour security", "1", "www.wrpsolution.com");
        Facility.FoFacilityList fontList7 = new Facility.FoFacilityList("Outside catering service", "1", "www.wrpsolution.com");
        Facility.FoFacilityList[] foHotelList = new Facility.FoFacilityList[]{fontList, fontList1, fontList2, fontList3, fontList4, fontList5};
        return foHotelList;
    }*/
    @OnClick(R.id.llBack)
    public void bacpress() {
        onBackPressed();
    }

    @OnClick(R.id.ivMenu)
    public void menuActivity() {
        Intent intent = new Intent(FacilityActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @OnClick(R.id.ibNavigation)
    public void changeActivity() {
        onBackPressed();
    }

}