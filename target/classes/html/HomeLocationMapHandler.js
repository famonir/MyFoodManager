class HomeLocationMapHandler {
    map;
    homeMarker = {};
    oldAddressMarker;
    lat = 0.0;
    lng = 0.0;
    constructor(mapToSet) {
        this.map = mapToSet;

    }

    getCustomIcon() {
        return L.Icon.extend({
            options: {
                iconSize: [65, 65],
                iconAnchor: [18, 35],
                popupAnchor: [0, -30]
            }
        });
    }

    // noinspection JSUnusedGlobalSymbols
    zoomOnCoordinates(latitude, longitude) {
        this.map.setView([latitude, longitude], 12);
    }

    // noinspection JSUnusedGlobalSymbols
    locateHome(latitude, longitude) {
        this.map.setView([latitude, longitude], 12);
        const customIcon = this.getCustomIcon();
        const homeIcon = new customIcon({iconUrl: '../img/home.png'});
        this.oldAddressMarker = L.marker([latitude, longitude], {icon: homeIcon}).bindPopup(window.location.pathname);
        this.oldAddressMarker.addTo(map);
    }


    // Letting the user to chose a home location - icon of a home
    putHomeLocation(e) {


        if (this.homeMarker !== undefined) {
            map.removeLayer(this.homeMarker);
        }
        if (this.oldAddressMarker !== undefined) {
            map.removeLayer(this.oldAddressMarker);
        }

        this.lat = e.latlng.lat;
        this.lng = e.latlng.lng;

        const customIcon = this.getCustomIcon();                                            // Changing the icon
        const homeIcon = new customIcon({iconUrl: '../img/home.png'});

        this.homeMarker = L.marker(e.latlng, {icon: homeIcon});                             // Setting the home
        this.homeMarker.addTo(map);                                                         // Displaying on the map
    }

    // noinspection JSUnusedGlobalSymbols
    getLatitude() {
        return this.lat;
    }

    // noinspection JSUnusedGlobalSymbols
    getLongitude() {
        return this.lng;
    }

}
