package com.lightbend.akka.sample;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.*;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

public class Greeter extends AbstractBehavior<Greeter.Greet> {

  @RequiredArgsConstructor
  public static final class Greet {
    public final String whom;
    public final ActorRef<Greeted> replyTo;
  }

  @RequiredArgsConstructor
  @ToString
  @EqualsAndHashCode
  public static final class Greeted {
    public final String whom;
    public final ActorRef<Greet> from;
  }

  public static Behavior<Greet> create() {
    return Behaviors.setup(Greeter::new);
  }

  private Greeter(ActorContext<Greet> context) {
    super(context);
  }

  @Override
  public Receive<Greet> createReceive() {
    return newReceiveBuilder().onMessage(Greet.class, this::onGreet).build();
  }

  private Behavior<Greet> onGreet(Greet command) {
    getContext().getLog().info("Hello {}!", command.whom);
    command.replyTo.tell(new Greeted(command.whom, getContext().getSelf()));
    return this;
  }
}

