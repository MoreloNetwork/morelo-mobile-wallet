# Przekazanie sesji — historia transakcji portfela

## Cel użytkownika
Naprawić zakładkę portfela, w której poza jedną prawdziwą transakcją wysłaną przez użytkownika (obecnie pending) wyświetlane są wpisy nagród bloku, które użytkownik uważa za niezwiązane z jego portfelem. Zachować prawdziwe transakcje incoming/outgoing i pending; nie czyścić danych portfela ani nie wykonywać transakcji.

## Repozytorium / środowisko
- Projekt aplikacji Android: `C:\Users\Krzychu\Documents\GitHub\xmrwallet` (to repozytorium należy edytować).
- Repozytorium kodu natywnego Morelo: `C:\Users\Krzychu\Documents\GitHub\morelo`.
- W sesji pracowaliśmy w PowerShell. Komenda `git` nie była dostępna w PATH.
- APK debug arm64 został zainstalowany wcześniej na urządzeniu Android 15 `RMX3867` (`D6EIPBHQ79YDPRMN`) przez `adb install -r`; dane aplikacji pozostawiono bez zmian. To było przed obecnym zgłoszeniem.

## Diagnoza wykonana
- `WalletFragment.onRefreshed()` pobiera `wallet.getHistory().getAll()` i wyświetla wpisy po filtrze wysokości/pending.
- JNI w `app/src/main/cpp/monerujo.cpp` zamienia `Monero::TransactionInfo` na Java `TransactionInfo`; obecnie filtruje konto, ale nie ma flagi coinbase.
- Java `TransactionInfo` nie ma właściwości coinbase.
- W kodzie natywnym Morelo (`src/wallet/api/transaction_history.cpp`) wejścia do historii pochodzą z `wallet2::get_payments()`, z walletowych `payment_details`. W standardowym skanowaniu te wpisy mają przypisany subadres portfela.
- `payment_details` w `src/wallet/wallet2.h` ma `m_coinbase`; w light-wallet `wallet_light_rpc.h` odpowiedź serwera ma pole `coinbase`, a `wallet2.cpp` zapisuje je w payment_details.
- Publiczny interfejs `TransactionInfo` (`external-libs/include/wallet2_api.h`) nie wystawia tego pola. Nie potwierdzono, że konkretne nagrody na ekranie są obce; ukrycie wszystkich transakcji coinbase może ukryć także prawidłową nagrodę dla portfela.
- W konsensusie Morelo znaleziono regułę `unlock_time == height + config::blockchain_settings::ARQMA_BLOCK_UNLOCK_CONFIRMATIONS`, gdzie wartość stałej to 18. Taki heurystyczny warunek nie jest bezpiecznym zamiennikiem jawnej flagi coinbase: zwykła transakcja może spełnić podobny warunek.

## Zmiany już wykonane (niezweryfikowane)
W repozytorium `xmrwallet` zmieniono:
1. `app/src/main/java/com/m2049r/xmrwallet/model/TransactionInfo.java`
   - Dodano stałą `BLOCK_REWARD_UNLOCK_BLOCKS = 18`.
   - Dodano `isBlockReward()` oparty o kierunek incoming, niepending, `blockheight > 0` i `unlockTime == blockheight + 18`.
2. `app/src/main/java/com/m2049r/xmrwallet/WalletFragment.java`
   - Lista pomija elementy, dla których `info.isBlockReward()` zwraca true.
   - Historia i dane salda nie są filtrowane; pending jest wyłączony z tej klasyfikacji.
3. Nowy plik `app/src/test/java/com/m2049r/xmrwallet/model/TransactionInfoTest.java`
   - Cztery testy: rozpoznanie reward, pending incoming zachowany, zwykły incoming zachowany, outgoing zachowany.

**Ważne:** zmiana Java jest tylko tymczasowym heurystycznym rozwiązaniem. Przed uznaniem jej za właściwą naprawę należy sprawdzić możliwość fałszywego ukrycia normalnego incoming. Preferowane rozwiązanie to przeniesienie prawdziwego `m_coinbase` przez natywny API `TransactionInfo` → JNI → Java, a następnie filtrowanie na liście tylko według jawnej flagi (jeśli wymaganiem nadal jest ukrywanie wszystkich nagród blokowych).

## Walidacja
- `get_errors` dla dwóch zmienionych Java plików i nowego testu zwrócił brak diagnostyk.
- Pierwsza komenda Gradle zakończyła się błędem niejednoznacznej nazwy taska: `testMainnetDebugUnitTest`; istnieją taski `testAlphaMainnetDebugUnitTest` oraz `testProdMainnetDebugUnitTest`.
- Próba uruchomienia dokładnych tasków `:app:testProdMainnetDebugUnitTest :app:assembleProdMainnetDebug --no-daemon` została anulowana. Build i testy nie są zaliczone.
- Nie uruchamiano exchange tests. Nie uruchamiaj ich w kolejnej sesji.

## Następne kroki
1. Otwórz to repozytorium i tę notatkę w nowej sesji; potwierdź obecność zmian i nie resetuj żadnych innych lokalnych zmian.
2. Zanim utrwalisz heurystykę, wdroż bezpieczne przekazanie jawnej flagi coinbase: zmodyfikuj natywny API w repozytorium `morelo` (`src/wallet/api/wallet2_api.h`, `transaction_info.h/.cpp`, `transaction_history.cpp`), odpowiedni publiczny nagłówek używany przez JNI (`xmrwallet/external-libs/include/wallet2_api.h`), mapper JNI i model Java. Utrzymaj zgodność konstruktora JNI. Dla wyłącznie wyświetlanego pola uwzględnij też Parcelable, jeśli flaga będzie częścią modelu serializowanego.
3. Zweryfikuj jak powstają wskazane nagrody i czy użytkownik chce ukrywać wszystkie reward, czy tylko te obce. Poprawkę ogranicz do listy; nie zmieniaj salda/history storage ani pending.
4. Przebuduj biblioteki natywne dla obsługiwanych ABI (co najmniej arm64-v8a i armeabi-v7a) zgodnie z konfiguracją `app/CMakeLists.txt`, zaktualizuj odpowiednie artefakty pod `xmrwallet/external-libs` wyłącznie jeśli repo ma procedurę ich budowania.
5. Uruchom dokładne taski wariantu, np. `:app:testProdMainnetDebugUnitTest` i `:app:assembleProdMainnetDebug`, jeśli są zgodne z bieżącymi flavorami. Uruchamiaj Gradle serialnie; nie łącz równoległych zadań korzystających ze wspólnych intermediates.
6. Zweryfikuj nowym testem: coinbase jest klasyfikowane zgodnie z jawna flagą; incoming nie-coinbase, outgoing i pending pozostają widoczne.

## Kontekst wcześniejszych prac
Poprzednie zadanie obejmowało testy aplikacji i instalację APK. Były już inne niezależne, niezatwierdzone modyfikacje w worktree (m.in. uprawnienia, zasoby, JNI, testy Android). Nie cofaj ani nie porządkuj ich przy tym zadaniu. Wcześniejsze testy 54 testów JVM oraz smoke test UI dotyczyły poprzedniego zadania i nie walidują tej poprawki.
