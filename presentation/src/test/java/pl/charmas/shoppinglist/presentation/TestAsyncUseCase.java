package pl.charmas.shoppinglist.presentation;

import pl.charmas.shoppinglist.domain.usecase.UseCase;
import pl.charmas.shoppinglist.domain.usecase.UseCaseArgumentless;
import pl.charmas.shoppinglist.presentation.async.AsyncUseCase;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class TestAsyncUseCase implements AsyncUseCase {

  public <T, A> Observable<T> wrap(final UseCase<T, A> useCase, final A arg) {
    return Observable.create(
        new Observable.OnSubscribe<T>() {
          @Override
          public void call(Subscriber<? super T> subscriber) {
            try {
              subscriber.onNext(useCase.execute(arg));
              subscriber.onCompleted();
            } catch (Exception e) {
              subscriber.onError(e);
            }
          }
        }
    ).subscribeOn(Schedulers.immediate()).observeOn(Schedulers.immediate());
  }

  public <T> Observable<T> wrap(final UseCaseArgumentless<T> useCase) {
    return Observable.create(
        new Observable.OnSubscribe<T>() {
          @Override
          public void call(Subscriber<? super T> subscriber) {
            try {
              subscriber.onNext(useCase.execute());
              subscriber.onCompleted();
            } catch (Exception e) {
              subscriber.onError(e);
            }
          }
        }
    ).subscribeOn(Schedulers.immediate()).observeOn(Schedulers.immediate());
  }
}
