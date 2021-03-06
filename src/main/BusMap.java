package main;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;

import com.teamdev.jxmaps.ControlPosition;
import com.teamdev.jxmaps.Icon;
import com.teamdev.jxmaps.InfoWindow;
import com.teamdev.jxmaps.LatLng;
import com.teamdev.jxmaps.MapOptions;
import com.teamdev.jxmaps.MapStatus;
import com.teamdev.jxmaps.MapTypeControlOptions;
import com.teamdev.jxmaps.Marker;
import com.teamdev.jxmaps.Polyline;
import com.teamdev.jxmaps.PolylineOptions;
import com.teamdev.jxmaps.swing.MapView;


@SuppressWarnings("serial")
public class BusMap extends MapView{
	
	InfoWindow infoWindow = null;
	
    public BusMap() {
    	setOnMapReadyHandler(this::onMapReady);
    }
    
    public void addBusline(List<LatLng> points , String color) {
    	// Creating a path (array of coordinates) that represents a polyline
    	LatLng[] path = new LatLng[points.size()];
    	path = points.toArray(path);
        
        // Creating a new busline object
        Polyline polyline = new Polyline(getMap());
        // Initializing the busline with created path
        polyline.setPath(path);
        // Creating a busline options object
        PolylineOptions options = new PolylineOptions();   
        
        // Setting geodesic property value
        options.setGeodesic(true);
        // Setting stroke color value
        options.setStrokeColor(color);
        // Setting stroke opacity value
        options.setStrokeOpacity(1.0);
        // Setting stroke weight value
        options.setStrokeWeight(2.0);
        // Applying options to the busline
        polyline.setOptions(options);
    }
    
    public Marker addBus(LatLng ll, String busLine, String busColor) throws FileNotFoundException {
    	Marker marker = new Marker(getMap());
    	
    	Icon icon = new Icon();
    	InputStream inputstream = getClass().getResourceAsStream("/res/bus" + busColor + ".png");
//    	InputStream inputstream = new FileInputStream("./res/bus" + busColor + ".png");
    	icon.loadFromStream(inputstream, "png");
    	marker.setIcon(icon);
    	
        marker.setPosition(ll);        
        
        // Adding event listener that intercepts clicking on marker
//        marker.addEventListener("click", new BusMarkerClickHandler(busLine, marker, getMap()));
        
        return marker;
    }
    
    /**
     * 
     * @param busNumber It also accept the busStop filename (old mode of the method)
     * @throws FileNotFoundException
     */
    public void addStops(String busNumber) throws FileNotFoundException {
    	
//    	BusPathParser stopsParser;
//    	List<LatLng> stopsPoints;
//    	stopsParser = new BusPathParser(filenameStops);
//    	stopsPoints = stopsParser.getListOfPoint();
    	
    	//replaceAll used to maintain compatibility with the old usage of the method
    	List<LatLng> stopsPoints = BusStopManager.getInstance().getStopsPoints(busNumber.replaceAll("[^\\d]", ""));
    	int sizeOfStopsList = stopsPoints.size();
    	Icon icon = new Icon();
    	InputStream inputstream = getClass().getResourceAsStream("/res/stop.png");
//    	InputStream inputstream = new FileInputStream("./res/stop.png");
    	icon.loadFromStream(inputstream, "png");
    	
    	for (int i=0; i<sizeOfStopsList; i++) {
	    	Marker marker = new Marker(getMap());
	    	marker.setIcon(icon);
	    	marker.setPosition(stopsPoints.get(i));	
	    	
	    	// Adding event listener that intercepts clicking on marker
//            marker.addEventListener("click", new BusStopMarkerClickHandler(BusStopManager.getInstance().getBusStopFromLatLngString(busNumber, stopsPoints.get(i).getLat() + "-" + stopsPoints.get(i).getLng()), getMap(), marker));
		}
    }
    
    public void moveBus(Marker m, LatLng ll) {
    	m.setPosition(ll);
    }
    
    public void onMapReady(MapStatus status) {
        // Check if the map is loaded correctly
        if (status == MapStatus.MAP_STATUS_OK) {
            // Creating a map options object
        	
            MapOptions mapOptions = new MapOptions();
            // Creating a map type control options object
            MapTypeControlOptions controlOptions = new MapTypeControlOptions();
            // Changing position of the map type control
            controlOptions.setPosition(ControlPosition.TOP_RIGHT);
            // Setting map type control options
            mapOptions.setMapTypeControlOptions(controlOptions);
            // Setting map options
            getMap().setOptions(mapOptions);
            // Setting the map center
            //getMap().setCenter(new LatLng(44.493889, 11.342778));
            // Setting initial zoom value
            getMap().setZoom(14.0);  
        }	
    }
}
