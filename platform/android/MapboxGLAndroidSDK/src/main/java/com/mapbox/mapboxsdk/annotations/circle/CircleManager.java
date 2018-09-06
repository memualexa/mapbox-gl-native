// This file is generated. Edit android/platform/scripts/generate-style-code.js, then run `make android-style-code`.

package com.mapbox.mapboxsdk.annotations.circle;

import android.graphics.PointF;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.UiThread;
import android.support.annotation.VisibleForTesting;
import android.support.v4.util.LongSparseArray;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.style.layers.PropertyValue;
import com.mapbox.mapboxsdk.style.layers.CircleLayer;
import com.mapbox.mapboxsdk.style.sources.GeoJsonSource;

import java.util.ArrayList;
import java.util.List;

import static com.mapbox.mapboxsdk.style.expressions.Expression.get;
import static com.mapbox.mapboxsdk.style.layers.PropertyFactory.*;

/**
 * The circle manager allows to add circles to a map.
 */
public class CircleManager {

  public static final String ID_GEOJSON_SOURCE = "mapbox-android-circle-source";
  public static final String ID_GEOJSON_LAYER = "mapbox-android-circle-layer";

  // map integration components
  private GeoJsonSource geoJsonSource;
  private CircleLayer layer;

  // callback listeners
  private OnCircleClickListener circleClickListener;

  // internal data set
  private final LongSparseArray<Circle> circles = new LongSparseArray<>();
  private final List<Feature> features = new ArrayList<>();
  private long currentId;

  /**
   * Create a circle manager, used to manage circles.
   *
   * @param mapboxMap the map object to add circles to
   */
  @UiThread
  public CircleManager(@NonNull MapboxMap mapboxMap) {
    this(mapboxMap, new GeoJsonSource(ID_GEOJSON_SOURCE), new CircleLayer(ID_GEOJSON_LAYER, ID_GEOJSON_SOURCE)
      .withProperties(
        getLayerDefinition()
      )
    );
  }

  /**
   * Create a circle manager, used to manage circles.
   *
   * @param mapboxMap     the map object to add circles to
   * @param geoJsonSource the geojson source to add circles to
   * @param layer         the circle layer to visualise Circles with
   */
  @VisibleForTesting
  public CircleManager(MapboxMap mapboxMap, @NonNull GeoJsonSource geoJsonSource, CircleLayer layer) {
    this.geoJsonSource = geoJsonSource;
    this.layer = layer;
    mapboxMap.addSource(geoJsonSource);
    mapboxMap.addLayer(layer);
    mapboxMap.addOnMapClickListener(new MapClickResolver(mapboxMap));
  }

  /**
   * Create a circle on the map from a LatLng coordinate.
   *
   * @param latLng place to layout the circle on the map
   * @return the newly created circle
   */
  @UiThread
  public Circle createCircle(@NonNull LatLng latLng) {
    Circle circle = new Circle(this, currentId);
    circle.setLatLng(latLng);
    circles.put(currentId, circle);
    currentId++;
    return circle;
  }

  /**
   * Delete a circle from the map.
   *
   * @param circle to be deleted
   */
  @UiThread
  public void deleteCircle(@NonNull Circle circle) {
    circles.remove(circle.getId());
    updateSource();
  }

  /**
   * Get a list of current circles.
   *
   * @return list of circles
   */
  @UiThread
  public LongSparseArray<Circle> getCircles() {
    return circles;
  }

  /**
   * Trigger an update to the underlying source
   */
  public void updateSource() {
    // todo move feature creation to a background thread?
    features.clear();
    Circle circle;
    for (int i = 0; i < circles.size(); i++) {
      circle = circles.valueAt(i);
      features.add(Feature.fromGeometry(circle.getGeometry(), circle.getFeature()));
    }
    geoJsonSource.setGeoJson(FeatureCollection.fromFeatures(features));
  }

  /**
   * Set a callback to be invoked when a circle has been clicked.
   * <p>
   * To unset, use a null argument.
   * </p>
   *
   * @param circleClickListener the callback to be invoked when a circle is clicked, or null to unset
   */
  public void setOnCircleClickListener(@Nullable OnCircleClickListener circleClickListener) {
    this.circleClickListener = circleClickListener;
  }

  private static PropertyValue<?>[] getLayerDefinition() {
    return new PropertyValue[]{
     circleRadius(get("circle-radius")),
     circleColor(get("circle-color")),
     circleBlur(get("circle-blur")),
     circleOpacity(get("circle-opacity")),
     circleStrokeWidth(get("circle-stroke-width")),
     circleStrokeColor(get("circle-stroke-color")),
     circleStrokeOpacity(get("circle-stroke-opacity")),
    };
  }

  // Property accessors
  /**
   * Get the CircleTranslate property
   *
   * @return property wrapper value around Float[]
   */
  public Float[] getCircleTranslate() {
    return layer.getCircleTranslate().value;
  }

  /**
   * Set the CircleTranslate property
   *
   * @param value property wrapper value around Float[]
   */
  public void setCircleTranslate(Float[] value) {
    layer.setProperties(circleTranslate(value));
  }

  /**
   * Get the CircleTranslateAnchor property
   *
   * @return property wrapper value around String
   */
  public String getCircleTranslateAnchor() {
    return layer.getCircleTranslateAnchor().value;
  }

  /**
   * Set the CircleTranslateAnchor property
   *
   * @param value property wrapper value around String
   */
  public void setCircleTranslateAnchor(String value) {
    layer.setProperties(circleTranslateAnchor(value));
  }

  /**
   * Get the CirclePitchScale property
   *
   * @return property wrapper value around String
   */
  public String getCirclePitchScale() {
    return layer.getCirclePitchScale().value;
  }

  /**
   * Set the CirclePitchScale property
   *
   * @param value property wrapper value around String
   */
  public void setCirclePitchScale(String value) {
    layer.setProperties(circlePitchScale(value));
  }

  /**
   * Get the CirclePitchAlignment property
   *
   * @return property wrapper value around String
   */
  public String getCirclePitchAlignment() {
    return layer.getCirclePitchAlignment().value;
  }

  /**
   * Set the CirclePitchAlignment property
   *
   * @param value property wrapper value around String
   */
  public void setCirclePitchAlignment(String value) {
    layer.setProperties(circlePitchAlignment(value));
  }

  /**
   * Inner class for transforming map click events into circle clicks
   */
  private class MapClickResolver implements MapboxMap.OnMapClickListener {

    private MapboxMap mapboxMap;

    private MapClickResolver(MapboxMap mapboxMap) {
      this.mapboxMap = mapboxMap;
    }

    @Override
    public void onMapClick(@NonNull LatLng point) {
      if (circleClickListener == null) {
        return;
      }

      PointF screenLocation = mapboxMap.getProjection().toScreenLocation(point);
      List<Feature> features = mapboxMap.queryRenderedFeatures(screenLocation, ID_GEOJSON_LAYER);
      if (!features.isEmpty()) {
        long circleId = features.get(0).getProperty(Circle.ID_KEY).getAsLong();
        Circle circle = circles.get(circleId);
        if (circle != null) {
          circleClickListener.onCircleClick(circles.get(circleId));
        }
      }
    }
  }

}