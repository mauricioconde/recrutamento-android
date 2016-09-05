package example.self.testapp.model.data;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Wrapper for manage a Show
 */
public class ShowPOJO {
    private String title;
    private int year;
    private IdsPOJO ids;
    private String score;

    public ShowPOJO(String title, int year, String score, IdsPOJO ids){
        this.title = title;
        this.year = year;
        this.score = score;
        this.ids = ids;
    }

    public static ArrayList<ShowPOJO> getShows(){
        ArrayList<ShowPOJO> shows = new ArrayList<>();
        shows.add(new ShowPOJO("Game of Thrones", 2011, "59.5", new IdsPOJO(1390,"game-of-thrones",121361,"tt0944947",1399,24493)));
        shows.add(new ShowPOJO("Smallville", 2001, "150.2", new IdsPOJO(4580,"smallville",72218,"tt0279600",4604,5227)));
        shows.add(new ShowPOJO("House", 2004, "51.7", new IdsPOJO(1399,"house",73255,"tt0412142",1408,3908)));

        return shows;
    }

    public String getTitle() {
        return title;
    }

    public int getYear() {
        return year;
    }

    public IdsPOJO getIds() {
        return ids;
    }

    public String getScore() {
        return score;
    }
}
