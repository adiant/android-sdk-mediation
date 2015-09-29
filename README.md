# Mediator SDK integration

This repository includes classes that should be used to display Adblade's ads using another SDK. If you wish to use Adblade's SDK to render Adblade's ads, then please read our [Getting started](https://github.com/adiant/android-sdk-example/wiki/Getting-started) guide.

## MoPub

### Add the Adblade SDK to your build

See our [Getting started](https://github.com/adiant/android-sdk-example/wiki/Getting-started) guide for instructions on including the Adblade SDK in your build.

### Add Adblade's custom event files to your code

If you wish to integrate **Banner** or **Interstitial** ads, create a **com.mopub.mobileads** package in your project. Then, copy to this package [AdbladeBanner.java](https://github.com/adiant/android-sdk-mediation/blob/master/src/main/java/com/mopub/mobileads/AdbladeBanner.java) for banner ads or [AdbladeInterstitial.java](https://github.com/adiant/android-sdk-mediation/blob/master/src/main/java/com/mopub/mobileads/AdbladeInterstitial.java) for interstitial ads.

If you wish to integrate **Native** ads, create a **com.mopub.nativeads** package in your project. Then, copy to this package [AdbladeNative.java](https://github.com/adiant/android-sdk-mediation/blob/master/src/main/java/com/mopub/nativeads/AdbladeNative.java).

You can integrate all three types of ads, a combination of them, or a single ad type.

### Enable custom events on the MoPub web interface

On the MoPub web interface, create a network with the **Custom Native Network** type. Copy the value from the table below into the **Custom Event Class** field for the type of Adblade ads you want to display.

Ad Type      | Custom Event Class
------------ | ------------------
Banner       | com.mopub.mobileads.AdbladeBanner
Interstitial | com.mopub.mobileads.AdbladeInterstitial
Native       | com.mopub.nativeads.AdbladeNative

Additionally, you must specify your Adblade container's ID in the **Custom Event Class Data** field. The contents of the field must be valid JSON. For example, if your container's ID is `12345-0001112220`, then you would enter this into the field:

```json
{"container_id": "12345-0001112220"}
```

### Add the Adblade activity to your manifest

This is required only to display Adblade's interstitial ads.

To display interstitial ads, you must add Adblade's **AdActivity** to your AndroidManifest.xml. Please see the [Interstitial ads](https://github.com/adiant/android-sdk-example/wiki/Interstitial-ads) guide for instructions.

### Render the native ad's display name

This is required only to display Adblade's native ads.

Adblade's native ads contain a "display name" field that is not directly supported by MoPub. It describes the advertiser, and it might ordinarily be prefixed with "Sponsored by." It has a maximum length of 25 characters.

To display this field, you will need to edit your native ad's layout XML to add a new TextView. Or, you can re-use an existing field in your native ad's layout XML.

Then, you must add an extra field when you create your **ViewBinder**. If the ID of the view that will display the "display name" is `textViewDisplayName`, then you would add this function call:

```java
addExtra(com.mopub.nativeads.AdbladeNative.EXTRA_DISPLAY_NAME, R.id.textViewDisplayName)
```

Afterward, your complete ViewBinder creation might look like this:

```java
ViewBinder binder = new ViewBinder.Builder(R.layout.list_item_native_ad)
    .mainImageId(R.id.imageView)
    .titleId(R.id.textViewTitle)
    .textId(R.id.textViewDescription)
    .addExtra(com.mopub.nativeads.AdbladeNative.EXTRA_DISPLAY_NAME, R.id.textViewDisplayName)
    .daaIconImageId(R.id.native_ad_daa_icon_image)
    .build();
```

### Display ads as usual

No other changes are required to your code. Use the MoPub dashboard to set up the corresponding network campaigns and target the proper ad units.

