import kNN3
import sys

trainingMat,labels = kNN3.getTrainingMat(sys.argv[2]);

#kNN3.classifyTest(trainingMat,labels,'testDigits');

print kNN3.classify(trainingMat,labels,sys.argv[1]);