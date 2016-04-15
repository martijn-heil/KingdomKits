package me.ninjoh.kingdomkits;


import me.ninjoh.nincore.api.common.org.jetbrains.annotations.NotNull;
import me.ninjoh.nincore.api.common.org.jetbrains.annotations.Nullable;
import net.mcapi.uuid.UUIDHandler;
import net.mcapi.uuid.queries.APIQuery;
import net.mcapi.uuid.queries.HistoryCallable;
import net.mcapi.uuid.utils.UUIDUtils;
import net.mcapi.uuid.utils.Username;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * The same UUID API handler as the default one, except this one does not spam console
 * with status messages about API requests.
 */
public class MainUUIDAPIHandler extends UUIDHandler
{
    @Nullable
    @Override
    public UUID getUUID(String username)
    {
        username = username.toLowerCase();
        if (uuid_cache.containsKey(username))
        {
            return uuid_cache.get(username);
        }

        APIQuery query = new APIQuery(username, "full_uuid", "uuid");

        try
        {
            UUID uuid = UUID.fromString(query.request());
            uuid_cache.put(username, uuid, 1, TimeUnit.HOURS);
            return uuid;
        }
        catch (Exception ex)
        {
            return null;
        }
    }


    @Nullable
    @Override
    public String getUUIDString(String username)
    {
        UUID UUID = getUUID(username);

        return UUID == null ? null : UUID.toString().replace("-", "");
    }


    @Nullable
    @Override
    public String getUsername(@NotNull UUID uuid)
    {
        if (name_cache.containsKey(uuid))
        {
            return name_cache.get(uuid);
        }

        APIQuery query = new APIQuery(uuid.toString().replace("-", ""), "name");

        try
        {
            String username = query.request();
            name_cache.put(uuid, username, 1, TimeUnit.HOURS);
            return username;
        }
        catch (Exception ex)
        {
            return null;
        }
    }


    @Nullable
    @Override
    public List<Username> getHistory(@NotNull UUID uuid)
    {
        List<Username> list = new LinkedList<>();

        HistoryCallable callable = new HistoryCallable(uuid.toString().replace("-", ""));
        List<Username> request;
        try
        {
            request = callable.request();

            if (request != null && !request.isEmpty())
            {
                list.addAll(request);
            }

            return list;
        }
        catch (Exception ex)
        {
            return null;
        }
    }


    @Nullable
    @Override
    public String getUsername(@NotNull String uuid)
    {
        return getUsername(UUID.fromString(UUIDUtils.convertUUIDToJava(uuid)));
    }
}
