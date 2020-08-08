package service;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class MovieClient {
  public static void main(String[] args) {
    // Create our client channel. Since we are running on localhost, and to keep things simple, we
    // will not be implementing security for this demo.
    ManagedChannel channel =
        ManagedChannelBuilder.forAddress("localhost", 9090)
            .usePlaintext() // No encryption
            .build();
    // Create a blocking (synchronous) client to work with. This allows us to treat method calls
    // like local calls. Alternatively, you could use a FutureStub if you wanted to use
    // multithreading and asynchronous calls to the service.
    MovieCatalogGrpc.MovieCatalogBlockingStub client = MovieCatalogGrpc.newBlockingStub(channel);
    // Create a movie object based on what we defined in our `proto` file to save it to the server
    MovieCatalogProto.MovieItem theMatrix =
        MovieCatalogProto.MovieItem.newBuilder()
            .setName("The Matrix")
            .setPrice(12.99)
            .setInStock(true)
            .build();
    // Make the save call to the server with our movie
    MovieCatalogProto.AddMovieResponse saveResponse = client.saveNewMovie(theMatrix);
    System.out.println(
        "Movie was saved: "
            + saveResponse.getWasSaved()
            + " / item id: "
            + saveResponse.getItemId());
    // Try to fetch the movie we just saved to the server by constructing a request object
    MovieCatalogProto.FetchMovieRequest fetchRequest =
        MovieCatalogProto.FetchMovieRequest.newBuilder().setName("The Matrix").build();
    MovieCatalogProto.MovieItem fetchResponse = client.fetchExistingMovie(fetchRequest);
    System.out.println(
        String.format(
            "Fetch movie %s at price $%f. In stock?: %s",
            fetchResponse.getName(), fetchResponse.getPrice(), fetchResponse.getInStock()));
  }
}
