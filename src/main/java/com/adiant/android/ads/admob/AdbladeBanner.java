package com.adiant.android.ads.admob;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.ViewGroup;

import com.adiant.android.ads.AdView;
import com.adiant.android.ads.core.ErrorReason;
import com.adiant.android.ads.util.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.mediation.MediationAdRequest;
import com.google.android.gms.ads.mediation.customevent.CustomEventBanner;
import com.google.android.gms.ads.mediation.customevent.CustomEventBannerListener;

import java.util.Map;
import java.util.TreeMap;

/**
 * Tested with Adblade 1.1.1
 */
public class AdbladeBanner implements CustomEventBanner {
    private static class AdbladeBannerEventForwarder extends AdListener {
        private final CustomEventBannerListener listener;
        private final AdView adView;

        public AdbladeBannerEventForwarder(AdView adView, CustomEventBannerListener listener) {
            this.adView = adView;
            this.listener = listener;
        }

        @Override
        public void onAdClicked() {
            listener.onAdClicked();
        }

        @Override
        public void onAdClosed() {
            listener.onAdClosed();
        }

        @Override
        public void onAdLeftApplication() {
            listener.onAdLeftApplication();
        }

        @Override
        public void onAdLoaded(Object ad) {
            listener.onAdLoaded(adView);
        }

        @Override
        public void onAdLoadFailed(ErrorReason error) {
            switch (error) {
                case NETWORK:
                    listener.onAdFailedToLoad(AdRequest.ERROR_CODE_NETWORK_ERROR);
                    break;
                case NO_FILL:
                    listener.onAdFailedToLoad(AdRequest.ERROR_CODE_NO_FILL);
                    break;
                case INTERNAL:
                default:
                    listener.onAdFailedToLoad(AdRequest.ERROR_CODE_INTERNAL_ERROR);
                    break;
            }
        }

        @Override
        public void onAdOpened() {
            listener.onAdOpened();
        }
    }

    private AdView adView;

    @Override
    public void requestBannerAd(Context context, CustomEventBannerListener listener, String adUnitId, AdSize adSize, MediationAdRequest mediationAdRequest, Bundle bundle) {
        if (adUnitId == null) {
            listener.onAdFailedToLoad(AdRequest.ERROR_CODE_INVALID_REQUEST);
            return;
        }

        final com.adiant.android.ads.core.AdSize size = calculateAdSize(adSize.getWidth(), adSize.getHeight());
        if (size == null) {
            listener.onAdFailedToLoad(AdRequest.ERROR_CODE_INVALID_REQUEST);
            return;
        }

        adView = new AdView(context);
        adView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));
        adView.setAdListener(new AdbladeBannerEventForwarder(adView, listener));
        adView.setAdUnitId(adUnitId);
        adView.setAdSize(size);
        adView.setSmart(true);
        adView.loadAd();
    }

    @Nullable
    private static com.adiant.android.ads.core.AdSize calculateAdSize(int width, int height) {
        // Use the smallest AdSize that will properly contain the adView
        for (Map.Entry<Integer, com.adiant.android.ads.core.AdSize> size : sizes().entrySet()) {
            if (height <= size.getKey()) {
                return size.getValue();
            }
        }
        return null;
    }

    private static Map<Integer, com.adiant.android.ads.core.AdSize> sizes() {
        // get all sizes, and sort by height ascending
        final Map<Integer, com.adiant.android.ads.core.AdSize> sizesByHeight = new TreeMap<>();
        for (com.adiant.android.ads.core.AdSize size : com.adiant.android.ads.core.AdSize.values()) {
            sizesByHeight.put(size.types().iterator().next().height(), size);
        }
        return sizesByHeight;
    }

    @Override
    public void onDestroy() {}

    @Override
    public void onPause() {}

    @Override
    public void onResume() {}
}
