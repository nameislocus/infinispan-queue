infinispan-queue
================

Queue Implementation using Infinispan Data Grid.

Infinispan (http://infinispan.org/)은 레드햇/JBoss의 오픈소스 분산 메모리 Key/Value 데이터 그리드 솔루션입니다.
Hazelcast나 redis와 같은 솔루션에서는 Queue와 같은 자료 구조를 제공하기 때문에 Key/Value 방식이외에 편리하게
사용할 수 있습니다.

Infinispan에서 Queue와 같은 자료구조를 Key / Value 구조만으로 Linked-List를 만들어 샘플을 구현했습니다.

자료구조에 대한 설명은 다음 slide를 참고하십시오.

http://www.slideshare.net/opennaru/20130226-infinispan-queue

