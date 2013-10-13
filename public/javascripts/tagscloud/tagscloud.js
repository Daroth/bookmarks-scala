'use strict';

window.TagsCloud = (function() {
	var process, processSize, maxPercent = 125, minPercent = 75;

	processSize = function(maxCount, minCount, weight) {
		var multiplier;

		if (maxCount == minCount) {
			maxCount += 1;
		}
		multiplier = (maxPercent - minPercent) / (maxCount - minCount)
		return minPercent
				+ ((maxCount - (maxCount - (weight - minCount))) * multiplier)

	}
	/**
	 * @param tags
	 *            list of tags [{"name": String, "weight": Int}]
	 */
	process = function(tags) {
		var countArray, maxCount, minCount, weight;

		weight = function(tag) {
			return tag.weight;
		};
		countArray = _.map(tags, weight);
		maxCount = _.max(tags, weight).weight;
		minCount = _.min(tags, weight).weight;

		return _.map(tags, function(tag) {
			tag.size = processSize(maxCount, minCount, tag.weight)
			return tag;
		});
	}

	return {
		process : process
	};
})();