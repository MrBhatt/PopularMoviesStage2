# PopularMoviesStage2

Update with your own Key for tmdb API requests. 

Replace <<<API_KEY>>> in enteries in String.xml with your own key:

    <string name="discoverMovieUrl"><![CDATA[https://api.themoviedb.org/3/discover/movie?sort_by=%s.desc&api_key=<<<API_KEY>>>]]></string>
    <string name="movieTrailerUrl"><![CDATA[https://api.themoviedb.org/3/movie/%s/videos?api_key=<<<API_KEY>>>]]></string>
    <string name="movieReviewUrl"><![CDATA[https://api.themoviedb.org/3/movie/%s/reviews?api_key=<<<API_KEY>>>]]></string>
