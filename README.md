## Telegram Nanoha Bot

This is the source code of [NanohaBot](https://t.me/NanohaBot), a telegram bot that you can use to search for anime and manga and send a summary to any chat.

### Usage

You can use NanohaBot by typing *@NanohaBot* in any chat input in Telegram.
This will open an auto complete popup where you can select the bot.

(See [Telegram documention](https://core.telegram.org/bots/inline) for more details about inline bots in Telegram)

After that you can type in the name of an anime or manga you're searching for.
Shortly after, a result list will be shown where you can select the desired entry.
When you select the entry the bot will send a message to the chat containing general information, a description and picture about the anime/manga.
It also contains a link to the corresponding MyAnimeList page.

You can also write this bot directly in which case the bot will interpret your message as a search query and returns the first search result.

#### Specifying the Search Type

You can specify if you want to search for anime or manga by adding a prefix.
Prefix your message with *a:* to search for anime and *m:* to search for manga.
Adding a *am:* prefix will search for both manga and anime.
This is the default behaviour if you don't add a prefix, so you can omit this.

#### Inline Examples:

Search for jojo manga:
````
@NanohaBot m: jojo
````

Search for jojo anime:
````
@NanohaBot a: jojo
````

Search for jojo manga and anime:
````
@NanohaBot jojo
````