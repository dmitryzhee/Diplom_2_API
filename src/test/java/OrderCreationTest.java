import com.google.gson.Gson;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;
import io.restassured.specification.RequestSpecification;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.Random;

public class OrderCreationTest implements TestData{

  private BurgersClient client = new BurgersClient();

  private RequestSpecification requestSpecification;

  private List<IngredientsData> ingredients;

  Random random;

  @Before
  public void setUp() {
    requestSpecification = new RequestSpecBuilder()
            .setBaseUri(BASE_URI)
            .setContentType(ContentType.JSON)
            .build();
    client.setRequestSpecification(requestSpecification);
    ingredients =  client.getIngredients().extract().body().as(Ingredients.class).getData();
    random = new Random();
//    for (int i=0; i<ingredients.size();i++) {
//      System.out.println(ingredients.get(i).get_id());
//    }
  }

  @Test
  public void orderCreationSuccess() {
    Gson gson = new Gson();
    String orderIngredient = ingredients.get(random.nextInt(ingredients.size())).get_id();
    OrderIngredients orderIngredients = new OrderIngredients(orderIngredient);
    String json = gson.toJson(orderIngredients);
    ValidatableResponse response = client.makeOrder(json);
    response.assertThat().statusCode(200);

  }
}
