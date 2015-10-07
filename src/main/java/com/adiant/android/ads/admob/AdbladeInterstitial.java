package com.adiant.android.ads.admob;

import android.app.Activity;

import com.adiant.android.ads.InterstitialAd;
import com.adiant.android.ads.core.ErrorReason;
import com.adiant.android.ads.util.AdListener;
import com.google.ads.mediation.MediationAdRequest;
import com.google.ads.mediation.customevent.CustomEventInterstitial;
import com.google.ads.mediation.customevent.CustomEventInterstitialListener;

/**
 * Tested with Adblade 1.1.1
 */
public class AdbladeInterstitial implements CustomEventInterstitial {
    private static class AdbladeInterstitialEventForwarder extends AdListener {
        private final CustomEventInterstitialListener listener;

        public AdbladeInterstitialEventForwarder(CustomEventInterstitialListener listener) {
            this.listener = listener;
        }

        @Override
        public void onAdClicked() {
            listener.onLeaveApplication();
        }

        @Override
        public void onAdClosed() {
            listener.onDismissScreen();
        }

        @Override
        public void onAdLeftApplication() {
            listener.onLeaveApplication();
        }

        @Override
        public void onAdLoaded(Object ad) {
            listener.onReceivedAd();
        }

        @Override
        public void onAdLoadFailed(ErrorReason error) {
            listener.onFailedToReceiveAd();
        }

        @Override
        public void onAdOpened() {
            listener.onReceivedAd();
        }
    }

    private InterstitialAd interstitial;

    @Override
    public void requestInterstitialAd(CustomEventInterstitialListener listener, Activity activity, String label, String adUnitId, MediationAdRequest mediationAdRequest, Object o) {
        if (adUnitId == null) {
            listener.onFailedToReceiveAd();
            return;
        }

        interstitial = new InterstitialAd(activity);
        interstitial.setAdListener(new AdbladeInterstitialEventForwarder(listener));
        interstitial.setAdUnitId(adUnitId);
        interstitial.loadAd();
    }

    @Override
    public void showInterstitial() {
        if (interstitial != null && interstitial.isLoaded()) {
            interstitial.show();
        }
    }

    @Override
    public void destroy() {
        if (interstitial != null) {
            interstitial.destroy();
            interstitial = null;
        }
    }
}
