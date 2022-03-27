// let xmlHttpRequest = new XMLHttpRequest();
//
function buttonPressed() {
//     xmlHttpRequest.open('GET', 'button-pressed', true)
//     xmlHttpRequest.onreadystatechange = processXml;
//     xmlHttpRequest.send();
// }

    $.ajax({
        type: "GET",
        dataType: "json",
        url: "button-pressed",
        // data: "action=loadall&id=" + id,
        complete: function (data) {
            let JSONObject = eval('(' + data.responseText + ')');
            let json = $.parseJSON(data.responseText); // create an object with the key of the array
            console.log(json); // where html is the key of array that you want, $response['html'] = "<a>something..</a>";
        }
    });
}

function showArtists(artists) {
    let artistItem = document.querySelector('.artistItem');
    let songItemContainer = document.querySelector('.artistItemContainer');
    songItemContainer.removeChild(artistItem)
    for (let x = 0; x < artists.length; x++) {
        let clone = artistItem.cloneNode(true);
        let artistName = clone.querySelector('.artistName');
        let heartEmpty = clone.querySelector('#heartEmpty');
        let heartFilled = clone.querySelector('#heartFilled');
        artistName.textContent = artists[x].name
        artistName.onclick = function () {
            goToAlbums(artists[x].name);
        }
        heartEmpty.onclick = function () {
            likeUnlikeArtist(artists[x].name, 'like', heartFilled, heartEmpty)
        }
        heartFilled.onclick = function () {
            likeUnlikeArtist(artists[x].name, 'unlike', heartFilled, heartEmpty)
        }
        if (artists[x].liked) {
            heartFilled.style.display = "block";
            heartEmpty.style.display = "none";
        } else if (!artists[x].liked) {
            heartFilled.style.display = "none";
            heartEmpty.style.display = "block";
        }
        console.log(artistName.textContent + ' ' + artists[x].liked);
        songItemContainer.appendChild(clone)
    }
}

function likeUnlikeArtist(artist, action, likedIcon, unlikedIcon) {
    let queryString = window.location.search;
    let urlParams = new URLSearchParams(queryString);
    switchLikeUnlikeIcons(action, likedIcon, unlikedIcon);
    $.ajax({
        type: "POST",
        url: "like-unlike",
        data: "action=" + action + "&data=artist&" + "artist=" + artist,
        complete: function () {
            console.log('Artist ' + action + ' ' + action);
        }
    });
}

function loadArtists() {
    $.ajax({
        type: "GET",
        dataType: "json",
        url: "load",
        data: "data=artists",
        complete: function (data) {
            // let JSONObject = eval('(' + data.responseText + ')');
            let artists = $.parseJSON(data.responseText); // create an object with the key of the array
            showArtists(artists)
            // for(let i = 0; i < artists.length; i++){
            //     console.log(artists[i])
            // }
        }
    });
}

function goToAlbums(artist) {
    window.location.assign('albums.html?artist=' + artist);
}

function loadAlbums() {
    let queryString = window.location.search;
    let urlParams = new URLSearchParams(queryString);
    let artist = urlParams.get('artist');
    let artistNameItem = document.querySelector('.albumList h1');
    artistNameItem.textContent = artist + ' albums';
    // console.log(artist);
    $.ajax({
        type: "GET",
        dataType: "json",
        url: "load",
        data: "data=albums&" + "artist=" + artist,
        complete: function (data) {
            // let JSONObject = eval('(' + data.responseText + ')');
            let albums = $.parseJSON(data.responseText); // create an object with the key of the array
            showAlbums(artist, albums);
            // for(let i = 0; i < artists.length; i++){
            //     console.log(artists[i])
            // }
        }
    });
}

function showAlbums(artist, albums) {
    let albumItem = document.querySelector('.albumItem');
    let albumItemContainer = document.querySelector('.albumItemContainer');
    albumItemContainer.removeChild(albumItem);
    for (let x = 0; x < albums.length; x++) {
        let clone = albumItem.cloneNode(true);
        let albumTitle = clone.querySelector('.albumTitle');
        let heartEmpty = clone.querySelector('#heartEmpty');
        let heartFilled = clone.querySelector('#heartFilled');
        albumTitle.textContent = albums[x].title;
        heartEmpty.onclick = function () {
            likeUnlikeAlbum(albums[x].title, 'like', heartFilled, heartEmpty)
        }
        heartFilled.onclick = function () {
            likeUnlikeAlbum(albums[x].title, 'unlike', heartFilled, heartEmpty)
        }
        if (albums[x].liked) {
            heartFilled.style.display = "block";
            heartEmpty.style.display = "none";
        } else if (!albums[x].liked) {
            heartFilled.style.display = "none";
            heartEmpty.style.display = "block";
        }
        albumTitle.onclick = function () {
            goToSongs(artist, albums[x].title);
        };
        console.log(albumTitle.textContent + ' ' + albums[x].liked);
        albumItemContainer.appendChild(clone);
    }
}

// function likeAlbum(album, likedIcon, unlikedIcon) {
//     unlikedIcon.style.display = "none";
//     likedIcon.style.display = "block";
//     console.log('Album ' + album + ' liked');
// }

function switchLikeUnlikeIcons(action, likedIcon, unlikedIcon) {
    if (action === 'like') {
        unlikedIcon.style.display = "none";
        likedIcon.style.display = "block";
    } else if (action === 'unlike') {
        unlikedIcon.style.display = "block";
        likedIcon.style.display = "none";
    }
}

function likeUnlikeAlbum(album, action, likedIcon, unlikedIcon) {
    let queryString = window.location.search;
    let urlParams = new URLSearchParams(queryString);
    let artist = urlParams.get('artist');
    switchLikeUnlikeIcons(action, likedIcon, unlikedIcon);
    $.ajax({
        type: "POST",
        url: "like-unlike",
        data: "action=" + action + "&data=album&" + "artist=" + artist + "&album=" + album,
        complete: function () {
            console.log('Album ' + album + ' ' + action);
        }
    });
}

function loadSongs() {
    let queryString = window.location.search;
    let urlParams = new URLSearchParams(queryString);
    let artist = urlParams.get('artist');
    let album = urlParams.get('album');
    console.log(artist + ' - ' + album + ' songs');
    let artistNameItem = document.querySelector('#artistName');
    let albumTitleItem = document.querySelector('#albumTitle');
    artistNameItem.textContent = artist;
    albumTitleItem.textContent = album + ' songs';
    $.ajax({
        type: "GET",
        dataType: "json",
        url: "load",
        data: "data=songs&" + "artist=" + artist + "&album=" + album,
        complete: function (data) {
            // let JSONObject = eval('(' + data.responseText + ')');
            let songs = $.parseJSON(data.responseText); // create an object with the key of the array
            showSongs(artist, album, songs);
            // for(let i = 0; i < artists.length; i++){
            //     console.log(artists[i])
            // }
        }
    });
}

function goToSongs(artist, album) {
    window.location.assign('songs.html?artist=' + artist + '&album=' + album);
}

function showSongs(artist, album, songs) {
    let songItem = document.querySelector('.songItem');
    let songItemContainer = document.querySelector('.songItemContainer');
    songItemContainer.removeChild(songItem);
    for (let x = 0; x < songs.length; x++) {
        let clone = songItem.cloneNode(true);
        let songTitle = clone.querySelector('.songTitle');
        let heartEmpty = clone.querySelector('#heartEmpty');
        let heartFilled = clone.querySelector('#heartFilled');
        songTitle.textContent = songs[x].title;
        heartEmpty.onclick = function () {
            likeUnlikeAlbum(songs[x].title, 'like', heartFilled, heartEmpty)
        }
        heartFilled.onclick = function () {
            likeUnlikeAlbum(songs[x].title, 'unlike', heartFilled, heartEmpty)
        }
        if (songs[x].liked) {
            heartFilled.style.display = "block";
            heartEmpty.style.display = "none";
        } else if (!songs[x].liked) {
            heartFilled.style.display = "none";
            heartEmpty.style.display = "block";
        }
        // console.log(albumTitle.textContent + ' ' + albums[x].liked);
        songItemContainer.appendChild(clone);
    }
}

// function processXml(){
//     if(xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
// let dom = (new DOMParser()).parseFromString(xmlHttpRequest.responseText, "text/xml");
// let songsTags = dom.getElementsByTagName('Songs');
// for(let i = 0; i < songsTags.length; i++){
//     let songTags = songsTags[i].getElementsByTagName('Song');
//     console.log(songTags.length)
//     for(let j = 0; j < songTags.length; j++){
//         console.log(songTags[j].childNodes[0].nodeValue);
//     }
// }
//     }
// }