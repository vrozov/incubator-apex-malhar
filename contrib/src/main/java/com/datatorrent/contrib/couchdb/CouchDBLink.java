package com.datatorrent.contrib.couchdb;

import com.datatorrent.api.annotation.ShipContainingJars;
import com.google.common.base.Preconditions;
import org.ektorp.CouchDbConnector;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.net.MalformedURLException;

/**
 * Creates a connection to Couchdb which is used to interact with the backend.
 *
 * @since 0.3.5
 */
@ShipContainingJars(classes = {CouchDbConnector.class})
public class CouchDBLink
{

  @Nonnull
  private final CouchDbConnector couchDb;

  /**
   * Creates a connection with default configuration properties<br></br>
   * Default url: http://localhost:5984<br></br>
   * Default username: <br></br>
   * Default password: <br></br>
   *
   * @param dbName name of the database
   * @throws MalformedURLException
   */
  public CouchDBLink(String dbName) throws MalformedURLException
  {
    this(null, null, null, dbName);
  }

  /**
   * Creates a connection with specific configuration properties<br></br>
   *
   * @param url      url of the couchdb server
   * @param userName
   * @param password
   * @param dbName   name of the database
   */
  public CouchDBLink(@Nullable String url, @Nullable String userName, @Nullable String password, String dbName)
  {
    Preconditions.checkNotNull(dbName, "databaseName");
    StdHttpClient.Builder builder = new StdHttpClient.Builder();
    if (url != null) {
      try {
        builder.url(url);
      } catch (MalformedURLException e) {
        throw new IllegalArgumentException(e.getMessage());
      }
    }
    if (userName != null) builder.username(userName);
    if (password != null) builder.password(password);

    HttpClient httpClient = builder.build();
    couchDb = new StdCouchDbInstance(httpClient).createConnector(dbName, true);
  }

  /**
   *
   * @return {@link CouchDbConnector} that is used to interact with CouchDb.
   */
  public CouchDbConnector getConnector()
  {
    return this.couchDb;
  }
}
