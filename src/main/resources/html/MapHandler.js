class MapHandler {
    map;
    homeMarker;
    closestPolyline = null;
    cheapestPolyline = null;
    tempMarker = null;
    storeMarkers = [];
    storesNames = [];
    tempCoordLng;
    tempCoordLat;
    closestCheapestStore;
    constructor(mapToSet) {
        this.map = mapToSet;

    }

    addStoreName(storeName) {
        this.storesNames.push(storeName);
    }

    loadComboBox() {
        let dataList = document.getElementById('storesNames');

        this.storesNames.forEach(value => {
            let option = document.createElement('option');
            option.innerHTML = value;
            option.value = value;
            dataList.appendChild(option);
        })
    }

    getCustomIcon() {
        return L.Icon.extend({
            options: {
                iconSize: [65, 65],
                iconAnchor: [24, 46],
                popupAnchor: [0, -40]
            }
        });
    }

    locateHome(latitude, longitude) {
        this.map.setView([latitude, longitude], 14);
        const customIcon = this.getCustomIcon();
        const homeIcon = new customIcon({iconUrl: '../img/home.png'});
        this.homeMarker = L.marker([latitude, longitude], {icon: homeIcon});
        this.homeMarker.addTo(map);
    }

    findClosest() {
        let distance = 40000;
        let closestStore;
        for (let i = 0; i < this.storeMarkers.length; i++) {
            let from = this.homeMarker.getLatLng();
            let to = this.storeMarkers[i].getLatLng();
            let distanceToStore = from.distanceTo(to);
            if (distanceToStore < distance) {
                distance = distanceToStore;
                closestStore = this.storeMarkers[i];
            }
        }
        return closestStore;
    }

    locateClosestStore() {
        let closestStore = this.findClosest();
        closestStore.openPopup();
        let fromToArray = [];
        fromToArray.push(this.homeMarker.getLatLng());
        fromToArray.push(closestStore.getLatLng());
        if (this.closestPolyline != null) {
            this.map.removeLayer(this.closestPolyline);
        }
        this.closestPolyline = L.polyline(fromToArray);
        this.closestPolyline.addTo(map);
    }



    switchToStoreMarker(lat, lng) {
        let selectedOption = document.getElementById("storesNames");
        let storeName = selectedOption.value;
        map.removeLayer(this.tempMarker);
        this.tempMarker = null;
        let popup = storeName + '<br> <input type="button" value="Delete" onclick="mapHandler.deleteMarker(' + lat.toString() + ', ' + lng.toString() + ')"/>'
            + '<input type="button" value="Products" onclick="mapHandler.displayProducts(\'' + storeName + '\')"/>';

        mapHandler.putStoreInDB(lat, lng, storeName, popup);
        document.getElementById('inputBox').style.display='none';
        //document.getElementById('StoreName').value="";
        //view.$server.addShop(lat,lng,storeName);
    }
    deletePolylines() {
        if (this.closestPolyline != null) {
            map.removeLayer(this.closestPolyline);
        }
        if (this.cheapestPolyline != null) {
            map.removeLayer(this.cheapestPolyline);
        }
    }

    deleteMarker(lat, lng) {

        var result = window.bridge.deleteStore(lng, lat);
        if (result){
            for (let i = 0; i < this.storeMarkers.length; i++) {
                if (this.storeMarkers[i].getLatLng().lat === lat && this.storeMarkers[i].getLatLng().lng === lng) {
                    if (this.findClosest() === this.storeMarkers[i]) {
                        map.removeLayer(this.closestPolyline);
                    }
                    if (this.storeMarkers[i] === this.closestCheapestStore) {
                        map.removeLayer(this.cheapestPolyline);
                    }
                    map.removeLayer(this.storeMarkers[i]);
                    this.storeMarkers.splice(i, 1);
                }
            }}

    }

    findClosestMarker(markers) {
        let smallestDistance = 1000000.0;
        for (let i = 0; i < markers.length; i++) {
            let closestMarker = markers[i];
            for (let i = 0; i < this.storeMarkers.length; i++) {
                let storeMarker = this.storeMarkers[i].getPopup().getContent().toString();
                if (storeMarker.includes(closestMarker)) {
                    let from = this.homeMarker.getLatLng();
                    let to = this.storeMarkers[i].getLatLng();

                    let distance = from.distanceTo(to);
                    if (distance < smallestDistance) {
                        smallestDistance = distance;
                        this.closestCheapestStore = this.storeMarkers[i];
                    }
                }
            }
        }
        let cheapestStoreString = "<b>Cheapest store : </b>".toString()
        let storeMarker = this.closestCheapestStore.getPopup().getContent().toString();
        if (!storeMarker.includes(cheapestStoreString)) {
            this.closestCheapestStore.getPopup().setContent(cheapestStoreString + this.closestCheapestStore.getPopup().getContent());
        }
        this.closestCheapestStore.openPopup();
        if (this.cheapestPolyline != null) {
            this.map.removeLayer(this.cheapestPolyline);
        }
        let fromToArray = [];
        fromToArray.push(this.homeMarker.getLatLng());
        fromToArray.push(this.closestCheapestStore.getLatLng());
        this.cheapestPolyline = L.polyline(fromToArray, {
            color : "red"
        });
        this.cheapestPolyline.addTo(map);
    }

    deleteAllStoreMarkers() {
        for (let i = 0; i < this.storeMarkers.length; i++) {
            map.removeLayer(this.storeMarkers[i]);
        }
        this.storeMarkers.splice(0, this.storeMarkers.length);
    }

    savedMarker(){
        mapHandler.switchToStoreMarker(mapHandler.tempCoordLat, mapHandler.tempCoordLng);
    }

    putTempMarker(e) {
        if (this.tempMarker === null) {  //This ensures that only one temp marker is on the map at a time.
            document.getElementById('inputBox').style.display='block';
            mapHandler.tempCoordLat = e.latlng.lat;
            mapHandler.tempCoordLng = e.latlng.lng;
            const customMarker = L.marker(e.latlng);
            customMarker.bindPopup('<input type="button" value="Delete" onclick="mapHandler.deleteTempMarker()"/>');
            this.tempMarker = customMarker;
            customMarker.addTo(map);
        }
    }

    deleteTempMarker() {
        if (this.tempMarker != null) {
            map.removeLayer(this.tempMarker);
            this.tempMarker = null;
            document.getElementById('inputBox').style.display='none';
        }
    }

    putStoreMarker(lat, lng, popup) {
        const customIcon = this.getCustomIcon();
        const marketIcon = new customIcon({iconUrl: '../img/cart.png'});
        const markerToAdd = L.marker([lat, lng], {icon: marketIcon}).bindPopup(popup);
        markerToAdd.addTo(map);
        this.storeMarkers.push(markerToAdd);
        return markerToAdd;
    }

    putStoreInDB(lat, lng, storeName, popup) {
        this.putStoreMarker(lat, lng, popup);
        window.bridge.addStore(lng, lat, storeName);
    }

    getMarker() {
        return this.tempMarker;
    }

    displayProducts(storeName) {
        window.bridge.goToSpecificStoreMenu(storeName);
    }
}
